package com.hdh.lifeup.redis;

import com.google.common.collect.Lists;
import com.hdh.lifeup.dto.UserInfoDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisOperatorTest {

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void keys() throws Exception {
    }

    @Test
    public void ttl() throws Exception {
    }

    @Test
    public void expire() throws Exception {
    }

    @Test
    public void del() throws Exception {
    }

    @Test
    public void set() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO()
                .setUserId(3L)
                .setAuthTypes(Lists.newArrayList("a", "bb", "ccc"))
                .setNickname("lwang");

        redisOperator.set(UserKey.TOKEN, userInfoDTO.getUserId(), userInfoDTO);
    }

    @Test
    public void opsForValue() {
        UserInfoDTO userInfoDTO = new UserInfoDTO()
                .setUserId(2L)
                .setAuthTypes(Lists.newArrayList("a", "bb", "ccc"))
                .setNickname("lwang");
        stringRedisTemplate.opsForValue().set(userInfoDTO.getUserId().toString(), userInfoDTO.toString());
    }

    @Test
    public void mset() throws Exception {
    }

    @Test
    public void setex() throws Exception {
    }

    @Test
    public void setnx() throws Exception {
    }

    @Test
    public void get() throws Exception {
        UserInfoDTO userInfoDTO = redisOperator.get(UserKey.TOKEN, 3L);
        System.out.println(userInfoDTO);
    }

    @Test
    public void getList() throws Exception {
    }

    @Test
    public void hset() throws Exception {
    }

    @Test
    public void hget() throws Exception {
    }

}