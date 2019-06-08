package com.hdh.lifeup.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.model.domain.UserAuthDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAuthMapperTest {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Test
    public void testSelectOne() {
        UserAuthDO userAuthDO = userAuthMapper.selectOne(
                new QueryWrapper<UserAuthDO>().eq("auth_type", "yb")
                        .eq("auth_identifier", "asfasdf")
        );
        System.out.println(userAuthDO);
    }
}