package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.UserAuthDTO;
import com.hdh.lifeup.dto.UserInfoDTO;

/**
 * UserAuthService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/22
 */
public interface UserAuthService extends BaseService<UserAuthDTO, Long> {

    /**
     * 第三方登录，取DB，都没有就插入，最后存到Redis，返回token
     * @param userAuthDTO 授权DTO
     * @param userInfoDTO 信息DTO
     * @return token
     */
    String oauthLogin(UserAuthDTO userAuthDTO, UserInfoDTO userInfoDTO);

    /**
     * 系统账号登录
     * @param userAuthDTO 授权DTO
     * @return token
     */
    String appLogin(UserAuthDTO userAuthDTO);


    /**
     * 系统账号注册
     * @param userInfoDTO 注册DTO
     * @return token
     */
    String register(UserInfoDTO userInfoDTO);
}