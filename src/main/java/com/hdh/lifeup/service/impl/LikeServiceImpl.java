package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.dao.LikeCountUserMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.constant.BizTypeConst;
import com.hdh.lifeup.model.domain.LikeCountUserDO;
import com.hdh.lifeup.model.dto.MarketGoodsDTO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.redis.KeyPrefix;
import com.hdh.lifeup.redis.LikeKey;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import com.hdh.lifeup.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static com.hdh.lifeup.model.enums.ActionEnum.LIKE_ACTIVITY;
import static com.hdh.lifeup.model.enums.ActionEnum.LIKE_TEAM;

/**
 * LikeServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2019/05/25
 */
@Slf4j
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    @Lazy
    private AsyncTaskService asyncTaskService;

    @Autowired
    private LikeCountUserMapper likeCountUserMapper;

    @Autowired
    private TeamTaskService teamTaskService;
    @Autowired
    private MarketGoodsService marketGoodsService;

    @Override
    public int doLike(Long userId, Long memberRecordId) {
        TeamMemberRecordDTO memberRecordDTO = teamMemberService.getOneMemberRecord(memberRecordId);
        // 哪条记录被谁点赞了
        long result = redisOperator.sadd(LikeKey.ACTIVITY, memberRecordId, userId);
        if (result == 0) {
            log.warn("【点赞动态】点赞失败，可能是重复点赞，userId = [{}], memberRecordId = [{}]",
                    userId, memberRecordId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }

        // 记录的创建者的点赞数加1
        Long creatorId = memberRecordDTO.getUserId();
        int likeCount = (int) redisOperator.incr(UserKey.LIKE_COUNT, creatorId);

        // 异步写库
        asyncTaskService.doLike(userId, memberRecordDTO);
        asyncTaskService.reportAction(userId, LIKE_ACTIVITY, memberRecordId, BizTypeConst.TEAM_MEMBER_RECORD);
        return (int) redisOperator.scard(LikeKey.ACTIVITY, memberRecordId);
    }

    @Override
    public int undoLike(Long userId, Long memberRecordId) {
        TeamMemberRecordDTO memberRecordDTO = teamMemberService.getOneMemberRecord(memberRecordId);
        long result = redisOperator.srem(LikeKey.ACTIVITY, memberRecordId, userId);
        if (result == 0) {
            log.warn("【点赞动态】点赞取消失败，没有点过赞或系统缓存异常，userId = [{}], memberRecordId = [{}]",
                    userId, memberRecordId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }
        // 记录的创建者的点赞数减1
        Long creatorId = memberRecordDTO.getUserId();
        long likeCount = redisOperator.decr(UserKey.LIKE_COUNT, creatorId);

        // 异步写库
        asyncTaskService.undoLike(userId, memberRecordDTO);
        return (int) redisOperator.scard(LikeKey.ACTIVITY, memberRecordId);
    }

    @Override
    public int getUserLikeCount(Long userId) {
        Long likeCount = redisOperator.get(UserKey.LIKE_COUNT, userId);
        if (likeCount == null) {
            LikeCountUserDO likeCountDO = likeCountUserMapper.selectById(userId);
            likeCount = (likeCountDO != null) ?
                    likeCountDO.getLikeCount().longValue() : 0;
            redisOperator.set(UserKey.LIKE_COUNT, userId, likeCount);
        }
        return likeCount.intValue();
    }

    @Override
    public int getUserExchangedLikeCount(Long userId) {
        Long likeCount = redisOperator.get(UserKey.LIKE_COUNT_EXCHANGED, userId);
        return likeCount == null ? 0 : likeCount.intValue();
    }

    @Override
    public int getRecordLikeCount(Long memberRecordId) {
        return (int) redisOperator.scard(LikeKey.ACTIVITY, memberRecordId);
    }

    @Override
    public <T> int isLike(KeyPrefix<T> keyPrefix, long sourceId, long userId) {
        return redisOperator.sismember(keyPrefix, sourceId, userId) ? 1 : 0;
    }

    @Override
    public int exchangeLike(Long userId, Integer count) {
        // 获取总赞数和已经兑换的赞数
        int userLikeCount = getUserLikeCount(userId);
        int exchangedLikeCount = getUserExchangedLikeCount(userId);
        // 如果总赞数少于已经兑换的赞数，说明不可兑换
        if (userLikeCount < exchangedLikeCount) {
            log.warn("【点赞兑换】userId = [{}], userLikeCount = [{}], exchangedLikeCount = [{}]",
                    userId, userLikeCount, count);
            throw new GlobalException(CodeMsgEnum.LIKE_COUNT_NOT_ENOUGH);
        }
        // 赞数不足则取剩余的全部去兑换
        int currCount = userLikeCount - exchangedLikeCount;
        count = Math.min(count, currCount);
        asyncTaskService.exchangeLike(userId, count);
        return count;
    }

    @Override
    public int doLikeTeam(Long userId, Long teamId) {
        TeamTaskDTO teamTaskDTO = teamTaskService.getOne(teamId);
        // 哪个团队被谁点赞了
        long result = redisOperator.sadd(LikeKey.TEAM, teamId, userId);
        if (result == 0) {
            log.warn("【点赞团队】点赞失败，可能是重复点赞，userId = [{}], teamId = [{}]",
                    userId, teamId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }

        // 异步写库
        asyncTaskService.doLike(userId, teamTaskDTO);
        asyncTaskService.reportAction(userId, LIKE_TEAM, teamId, BizTypeConst.TEAM_TASK);
        return (int) redisOperator.scard(LikeKey.TEAM, teamId);
    }

    @Override
    public int undoLikeTeam(Long userId, Long teamId) {
        TeamTaskDTO teamTaskDTO = teamTaskService.getOne(teamId);
        long result = redisOperator.srem(LikeKey.TEAM, teamId, userId);
        if (result == 0) {
            log.warn("【点赞团队】点赞取消失败，没有点过赞或系统缓存异常，userId = [{}], teamId = [{}]",
                    userId, teamId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }

        // 异步写库
        asyncTaskService.undoLike(userId, teamTaskDTO);
        return (int) redisOperator.scard(LikeKey.TEAM, teamId);
    }

    @Override
    public int getTeamLikeCount(Long teamId) {
        return (int) redisOperator.scard(LikeKey.TEAM, teamId);
    }

    @Override
    public int doLikeGoods(Long userId, Long goodsId) {
        MarketGoodsDTO marketGoodsDTO = marketGoodsService.getOne(goodsId);
        // 哪个团队被谁点赞了
        long result = redisOperator.sadd(LikeKey.GOODS, goodsId, userId);
        if (result == 0) {
            log.warn("【点赞团队】点赞失败，可能是重复点赞，userId = [{}], goodsId = [{}]",
                userId, goodsId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }

        // 异步写库
        asyncTaskService.doLike(userId, marketGoodsDTO);
//        asyncTaskService.reportAction(userId, LIKE_TEAM, goodsId, BizTypeConst.TEAM_TASK);
        return (int) redisOperator.scard(LikeKey.GOODS, goodsId);
    }

    @Override
    public int undoLikeGoods(Long userId, Long goodsId) {
        MarketGoodsDTO marketGoodsDTO = marketGoodsService.getOne(goodsId);
        long result = redisOperator.srem(LikeKey.GOODS, goodsId, userId);
        if (result == 0) {
            log.warn("【点赞团队】点赞取消失败，没有点过赞或系统缓存异常，userId = [{}], goodsId = [{}]",
                userId, goodsId);
            throw new GlobalException(CodeMsgEnum.LIKE_ERROR);
        }

        // 异步写库
        asyncTaskService.undoLike(userId, marketGoodsDTO);
        return (int) redisOperator.scard(LikeKey.GOODS, goodsId);
    }

    @Override
    public int getGoodsLikeCount(Long goodsId) {
        return (int) redisOperator.scard(LikeKey.GOODS, goodsId);
    }
}
