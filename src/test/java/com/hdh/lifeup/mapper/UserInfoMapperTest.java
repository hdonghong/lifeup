package com.hdh.lifeup.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdh.lifeup.domain.UserInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoMapperTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void testInsert() {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setNickName("nick")
                .setUserPhone("15522112951")
                .setUserAddress("2018-08-18 08:44:12 广东省广州市天河区龙口东路创逸大厦11202")
                .setUserPassword("a");
        Integer result = userInfoMapper.insert(userInfoDO);
        assertEquals(1, result.intValue());
        log.info("user = [{}]", userInfoDO);
    }

    @Test
    public void testSelectById() {
        UserInfoDO userInfoDO = userInfoMapper.selectById(1030668013010395138L);
        log.info("user = [{}]", userInfoDO);
    }

    @Test
    public void testDelete() {
        userInfoMapper.deleteById(1030668013010395138L);
    }

    @Test
    public void testList() {
        List<UserInfoDO> userList = userInfoMapper.selectList(
                new QueryWrapper<>()
        );

        log.info("userList = [{}]", userList);
    }

    @Test
    public void testSelectPage() {
        Page<UserInfoDO> objectPage = new Page<>(2, 10);
        objectPage.setOptimizeCountSql(false);
        IPage<UserInfoDO> taskDOIPage = userInfoMapper.selectPage(
                objectPage,
                new QueryWrapper<>()
        );

        log.info("taskDOIPage = [{}]", taskDOIPage.toString());
    }

}