package com.hdh.lifeup.service;

import java.util.Objects;
import com.hdh.lifeup.dao.LikeCountUserMapper;
import com.hdh.lifeup.dao.LikeMemberRecordMapper;
import com.hdh.lifeup.exception.AsyncException;
import com.hdh.lifeup.model.domain.LikeCountUserDO;
import com.hdh.lifeup.model.domain.LikeMemberRecordDO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AsyncTaskService class<br/>
 * 异步任务类
 * @author hdonghong
 * @since 2019/06/05
 */
@Slf4j
@Component
public class AsyncTaskService {


    @Autowired
    private LikeMemberRecordMapper likeMemberRecordMapper;

    @Autowired
    private LikeCountUserMapper likeCountUserMapper;

    @Autowired
    private RedisOperator redisOperator;

    @Async("taskExecutor")
    public void doLike(Long userId, TeamMemberRecordDTO memberRecordDTO){
        LikeMemberRecordDO likeMemberRecordDO = new LikeMemberRecordDO()
                .setMemberRecordId(memberRecordDTO.getMemberRecordId())
                .setUserId(userId);
        Integer result = likeMemberRecordMapper.insert(likeMemberRecordDO);
        if (!Objects.equals(result, 1)) {
            log.error("【doLike】likeMemberRecordMapper.insert failed. userId = [{}], memberRecordDTO = [{}]",
                    userId, memberRecordDTO);
            return;
        }
        result = likeCountUserMapper.incr(userId, 1);
        if (!Objects.equals(result, 1)) {
            LikeCountUserDO likeCountUserDO = new LikeCountUserDO()
                    .setUserId(memberRecordDTO.getUserId())
                    .setLikeCount(1);
            likeCountUserMapper.insert(likeCountUserDO);
        }
    }

    @Async("taskExecutor")
    public void undoLike(Long userId, TeamMemberRecordDTO memberRecordDTO) {
        Integer result = likeMemberRecordMapper.deleteById(memberRecordDTO.getMemberRecordId());
        if (!Objects.equals(result, 1)) {
            log.error("【undoLike】likeMemberRecordMapper.deleteById failed. userId = [{}], memberRecordDTO = [{}]",
                    userId, memberRecordDTO);
            return;
        }
        likeCountUserMapper.incr(userId, -1);
    }

    @Async("taskExecutor")
    public void exchangeLike(Long userId, int count) {
//        redisOperator.decrby(UserKey.LIKE_COUNT, userId, count);
        redisOperator.incrby(UserKey.LIKE_COUNT_EXCHANGED, userId, count);
        likeCountUserMapper.incr(userId, -count);
    }


    @Async("taskExecutor")
    public void doTaskFour(Future<?> future) throws AsyncException {
        try {
            System.err.println("future = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
