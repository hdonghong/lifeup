package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import com.hdh.lifeup.service.LikeService;
import com.hdh.lifeup.service.TeamMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void doLike(Long userId, Long memberRecordId) {
//        teamMemberService.get
        // 谁点赞了某条记录
        redisOperator.sadd(UserKey.LIKE_ACTIVITY, userId, memberRecordId);
    }
}
