package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceImplTest {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void getOne() throws Exception {
    }

    @Test
    public void listByConditions() throws Exception {
    }

    @Test
    public void pageByConditions() throws Exception {
    }

    @Test
    public void save() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(1L)
                .setNickName("bbb")
                .setUserAddress("广东省广州市天河区龙口东路创逸大厦11202")
                ;
        Assert.assertEquals("", "");
    }

    @Test
    public void insert() throws Exception {
        UserInfoDTO userInfoDTO = userInfoService.insert(null);

    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void deleteLogically() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

}