package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.TokenContext;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.dao.UserInfoMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.constant.TaskConst;
import com.hdh.lifeup.model.domain.UserInfoDO;
import com.hdh.lifeup.model.dto.AttributeDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.UserDetailVO;
import com.hdh.lifeup.model.vo.UserListVO;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import com.hdh.lifeup.service.*;
import com.hdh.lifeup.util.PasswordUtil;
import com.hdh.lifeup.util.TokenUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hdh.lifeup.model.constant.UserConst.FollowStatus;

/**
 * UserInfoServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private RedisOperator redisOperator;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private AttributeService attributeService;

    @Resource
    private TeamMemberService memberService;
    @Resource
    private LikeService likeService;
    @Resource
    private UserRankService userRankService;

    @Override
    public UserInfoDTO getOne(@NonNull Long userId) {
        UserInfoDO userInfoDO = userInfoMapper.selectById(userId);
        if (userInfoDO == null) {
            log.error("【获取用户】不存在的用户，userId = [{}]", userId);
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        return BaseDTO.from(userInfoDO, UserInfoDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO insert(@NonNull UserInfoDTO userInfoDTO) {
        userInfoDTO.setPwdSalt(PasswordUtil.getSalt());
        UserInfoDO userInfoDO = userInfoDTO.toDO(UserInfoDO.class);
        Integer result = userInfoMapper.insert(userInfoDO);
        if (!Objects.equals(1, result)) {
            log.error("【新增用户信息】插入记录数量 = [{}], UserInfoDTO = [{}]", result, userInfoDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        // 新建账号的时候需要顺便新建人物的属性表
        AttributeDTO attributeDTO = new AttributeDTO()
                                        .setUserId(userInfoDO.getUserId());
        attributeService.insert(attributeDTO);
        return userInfoDTO.setUserId(userInfoDO.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO update(@NonNull UserInfoDTO userInfoDTO) {
        UserInfoDTO cachedUserInfoDTO = UserContext.get();
        BeanUtils.copyProperties(userInfoDTO, cachedUserInfoDTO, "userId", "createTime");
        Integer result = userInfoMapper.updateById(cachedUserInfoDTO.toDO(UserInfoDO.class));
        if (!Objects.equals(1, result)) {
            log.error("【修改用户信息】插入记录数量 = [{}], UserInfoDTO = [{}]", result, userInfoDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        redisOperator.setex(UserKey.TOKEN, TokenContext.get(), cachedUserInfoDTO);
        return cachedUserInfoDTO;
    }

    @Override
    public UserInfoDTO getByToken(String authenticityToken) {
        Preconditions.checkNotNull(authenticityToken, "【通过token获取用户】传入的Token为空");

        UserInfoDTO userInfoDTO = redisOperator.get(UserKey.TOKEN, authenticityToken);
        if (userInfoDTO == null) {
            log.info("【通过token获取用户】无效的TOKEN");
            return null;
        }

        long expire = redisOperator.ttl(UserKey.TOKEN, authenticityToken);
        if (expire < TokenUtil.MIN_EXPIRED) {
            log.info("【通过token获取用户】当前用户Token有效时长expire = [{}], 重设", expire);
            redisOperator.expire(UserKey.TOKEN, authenticityToken);
        }
        return userInfoDTO;
    }

    @Override
    public UserDetailVO getDetailById(Long userId) {
        // 当传入的用户id为空时，返回当前登录的用户信息
        UserInfoDTO userInfoDTO = (userId == null) ?
                UserContext.get() : this.getOne(userId);

        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(userInfoDTO, userDetailVO);
        // 加入的团队数量（进行中的）
        userId = userInfoDTO.getUserId();
        userDetailVO.setTeamAmount(memberService.countUserTeamsWithStatus(userId, TaskConst.TaskStatus.DOING));
//        userDetailVO.setTeamAmount(memberService.countUserTeams(userId));
        // 粉丝数量
        userDetailVO.setFollowerAmount(redisOperator.zcard(UserKey.FOLLOWER, userId));
        // 关注的人的数量
        userDetailVO.setFollowingAmount(redisOperator.zcard(UserKey.FOLLOWING, userId));
        // 关注的状态
        userDetailVO.setIsFollow(getFollowStatus(UserContext.get().getUserId(), userId));
        // 获赞数量
        userDetailVO.setLikeCount(likeService.getUserLikeCount(userId));

        return userDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void follow(Long userId) {
        // 当前用户是跟随者
        UserInfoDTO follower = UserContext.get();
        if (follower.getUserId().equals(userId)) {
            throw new GlobalException(CodeMsgEnum.FORBID_FOLLOW_YOURSELF);
        }
        // 判断userId指向的用户是否存在
        this.getOne(userId);

        // 存到跟随者 关注的用户列表中
        Long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        long addFollowingResult = redisOperator.zadd(UserKey.FOLLOWING, follower.getUserId(), nowSecond, userId);
        // 存到被关注用户的跟随者列表中
        long addFollowerResult = redisOperator.zadd(UserKey.FOLLOWER, userId, nowSecond, follower.getUserId());
        // 两个结果必须都等于1
        if (addFollowingResult != 1 || addFollowerResult != 1) {
            log.error("【关注用户】已关注或者其它异常情况，addFollowingResult = [{}], addFollowerResult = [{}]",
                    addFollowingResult, addFollowerResult);
            throw new GlobalException(CodeMsgEnum.FOLLOW_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFollowing(Long userId) {
        // 当前用户是跟随者
        UserInfoDTO follower = UserContext.get();
        if (follower.getUserId().equals(userId)) {
            throw new GlobalException(CodeMsgEnum.FORBID_FOLLOW_YOURSELF);
        }
        // 判断userId指向的用户是否存在
        this.getOne(userId);

        // 我关注的用户列表中移除此用户
        long remFollowingResult = redisOperator.zrem(UserKey.FOLLOWING, follower.getUserId(), userId);
        // 此用户的粉丝中移除我
        long remFollowerResult = redisOperator.zrem(UserKey.FOLLOWER, userId, follower.getUserId());
        // 两个结果必须都等于1
        if (remFollowingResult != 1 || remFollowerResult != 1) {
            log.error("【取消关注】已取消关注或者其它异常情况，remFollowingResult = [{}], remFollowerResult = [{}]",
                    remFollowingResult, remFollowerResult);
            throw new GlobalException(CodeMsgEnum.FOLLOW_ERROR);
        }
    }

    @Override
    public PageDTO<UserListVO> getFollowings(Long userId, PageDTO pageDTO) {
        if (userId == null) {
            userId = UserContext.get().getUserId();
        }
        return getUserListVOs(userId, pageDTO, UserKey.FOLLOWING);
    }

    @Override
    public PageDTO<UserListVO> getFollowers(Long userId, PageDTO pageDTO) {
        if (userId == null) {
            userId = UserContext.get().getUserId();
        }
        return getUserListVOs(userId, pageDTO, UserKey.FOLLOWER);
    }

    @Override
    @Deprecated // FIXME 极丑的实现
    public PageDTO<UserListVO> getFollowingsRank(Long userId, PageDTO pageDTO) {
        Set<Long> userIdSet = redisOperator.zrange(UserKey.FOLLOWING, userId, 0, -1);
        userIdSet.add(userId);
        List<UserInfoDO> userInfoDOList = userInfoMapper.selectList(
                new QueryWrapper<UserInfoDO>().in("user_id", userIdSet)
        );

        int fromIndex = (int) ((pageDTO.getCurrentPage() - 1) * pageDTO.getSize());
        int toIndex = fromIndex + pageDTO.getSize().intValue();
        int userSize = userIdSet.size();
        if (fromIndex >= userSize) {
            return PageDTO.emptyPage((long) Math.ceil((userSize * 1.0) / pageDTO.getSize()));
        } else if (toIndex > userSize) {
            toIndex = userSize;
        }

        List<UserListVO> userList = Lists.newArrayListWithCapacity(userSize);
        userInfoDOList.forEach(userInfoDO -> {
            UserListVO userListVO = new UserListVO();
            userList.add(userListVO);
            BeanUtils.copyProperties(userInfoDO, userListVO);
//            int attribute = memberService.getAttributeWeekly(userInfoDO.getUserId());
            int attribute = userRankService.getRankByUser(userInfoDO.getUserId()).getRankValue().intValue();
            userListVO.setPoint(attribute);
        });
        userList.sort((o1, o2) -> o2.getPoint() - o1.getPoint());
        for (int i = 0, len = userList.size(); i < len; i++) {
            userList.get(i).setRank(i + 1);
        }

        return PageDTO.<UserListVO>builder()
                .currentPage(pageDTO.getCurrentPage())
                .list(userList.subList(fromIndex, toIndex))
                .totalPage((long) Math.ceil((userSize * 1.0) / pageDTO.getSize()))
                .build();
    }

    @Override
    public List<UserInfoDTO> listUser(int current, int limit) {
        IPage<UserInfoDO> userInfoDOPage = userInfoMapper.selectPage(
                new Page<>(current, limit),
                new QueryWrapper<>()
        );
        return userInfoDOPage.getRecords().stream()
                .map(userInfoDO -> UserInfoDTO.from(userInfoDO, UserInfoDTO.class))
                .collect(Collectors.toList());
    }

    private PageDTO<UserListVO> getUserListVOs(Long userId, PageDTO pageDTO, UserKey<Long> userKey) {
        Set<Long> userIdSet = redisOperator.zrange(userKey, userId, 0, -1);

        int fromIndex = (int) ((pageDTO.getCurrentPage() - 1) * pageDTO.getSize());
        int toIndex = fromIndex + pageDTO.getSize().intValue();
        int userSize = userIdSet.size();
        if (fromIndex >= userSize) {
            return PageDTO.emptyPage((long) Math.ceil((userSize * 1.0) / pageDTO.getSize()));
        } else if (toIndex > userSize) {
            toIndex = userSize;
        }

        List<Long> userIdList = Lists.newArrayList(userIdSet)
                .subList(fromIndex, toIndex);
        List<UserInfoDO> userInfoDOList = userInfoMapper.selectList(
                new QueryWrapper<UserInfoDO>().in("user_id", userIdList)
        );
        List<UserListVO> userList = Lists.newArrayListWithCapacity(userInfoDOList.size());
        Long currentUserId = UserContext.get().getUserId();
        userInfoDOList.forEach(userDO -> {
            UserListVO userListVO = new UserListVO();
            BeanUtils.copyProperties(userDO, userListVO);
            // 判断用户列表与当前用户的关系
            int followStatus = getFollowStatus(currentUserId, userDO.getUserId());
            userListVO.setIsFollow(followStatus);
            userList.add(userListVO);
        });

        return PageDTO.<UserListVO>builder()
                      .currentPage(pageDTO.getCurrentPage())
                      .list(userList)
                      .totalPage((long) Math.ceil((userSize * 1.0) / pageDTO.getSize()))
                      .build();
    }

    /**
     * 判断b用户对于a用户而言的身份是否
     * @param aUserId a用户id
     * @param bUserId b用户id
     * @return 身份
     */
    private int getFollowStatus(long aUserId, long bUserId) {
        if (aUserId == bUserId) {
            return FollowStatus.MYSELF;
        }
        // 先判断 b 是否 a关注的人
        int aFollow = redisOperator.zrank(UserKey.FOLLOWING, aUserId, bUserId) != null ?
                FollowStatus.FOLLOWING : FollowStatus.NOT_FOLLOW;
        // 再判断 b 是否 a的粉丝
        int bFollow = redisOperator.zrank(UserKey.FOLLOWER, aUserId, bUserId) != null ?
                FollowStatus.FOLLOWING : FollowStatus.NOT_FOLLOW;

        if (FollowStatus.FOLLOWING.equals(aFollow)) {
            // b 是 a关注的人
            if (FollowStatus.FOLLOWING.equals(bFollow)) {
                // b 是否 a的粉丝 -> 互相关注
                return FollowStatus.INTERACTIVE;
            }
            return FollowStatus.FOLLOWING;
        }
        return FollowStatus.NOT_FOLLOW;
    }

}
