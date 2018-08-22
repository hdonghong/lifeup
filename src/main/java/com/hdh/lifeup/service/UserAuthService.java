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
     * 取DB，都没有就插入，最后存到Redis，返回token
     * @param userAuthDTO 授权DTO
     * @param userInfoDTO 信息DTO
     * @return token
     */
    String login(UserAuthDTO userAuthDTO, UserInfoDTO userInfoDTO);
}
