package com.hdh.lifeup.base;

import com.hdh.lifeup.domain.UserInfoDO;
import com.hdh.lifeup.dto.UserInfoDTO;
import org.junit.Test;

public class BaseDTOTest {

    @Test
    public void toDO() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(1L)
                .setNickname("bbb")
                .setUserAddress("广东省广州市天河区龙口东路创逸大厦11202");
        UserInfoDO userInfoDO = userInfoDTO.toDO(UserInfoDO.class);
        System.out.println(userInfoDO);
    }


    @Test
    public void from() throws Exception {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setNickname("nick")
                .setUserAddress("2018-08-18 08:44:12 广东省广州市天河区龙口东路创逸大厦11202");
        UserInfoDTO userInfoDTO = BaseDTO.from(userInfoDO, UserInfoDTO.class);
        System.out.println(userInfoDTO);
    }


    @Test
    public void from1() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setNickname("nick")
                .setUserAddress("2018-08-18 08:44:12 广东省广州市天河区龙口东路创逸大厦11202");
        userInfoDTO = userInfoDTO.from(userInfoDO);
        System.out.println(userInfoDTO);
    }

}