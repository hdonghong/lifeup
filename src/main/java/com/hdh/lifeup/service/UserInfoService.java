package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.vo.UserDetailVO;

/**
 * UserInfoService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
public interface UserInfoService extends BaseService<UserInfoDTO, Long> {

    /**
     * 缓存取，缓存存，通过token获取user
     * @param authenticityToken 鉴权Token
     * @return 用户信息
     */
    UserInfoDTO getByToken(String authenticityToken);

    /**
     * 获取用户详情
     * @param userId 用户id
     * @return 用户详情
     */
    UserDetailVO getDetailById(Long userId);

}
