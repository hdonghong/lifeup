package com.hdh.lifeup.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hdh.lifeup.constant.AuthTypeConst;
import com.hdh.lifeup.domain.UserInfoDO;
import org.junit.Test;

public class UserInfoDTOTest {


    @Test
    public void toDO() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(1L)
                .setNickname("bbb")
                .setUserAddress("广东省广州市天河区龙口东路创逸大厦11202")
                .setAuthTypes(Lists.newArrayList(AuthTypeConst.YB, AuthTypeConst.QQ));
        UserInfoDO userInfoDO = userInfoDTO.toDO(UserInfoDO.class);
        String str = new ObjectMapper().writeValueAsString(userInfoDO);
        System.out.println(str);

        UserInfoDTO from = UserInfoDTO.from(userInfoDO, UserInfoDTO.class);
        System.out.println(from);
    }

}