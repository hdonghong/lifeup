package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.service.LikeService;
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

    @Override
    public void doLike(Long userId, Long memberRecordId) {
        redisOperator.sadd(null, userId, memberRecordId);
    }
}
