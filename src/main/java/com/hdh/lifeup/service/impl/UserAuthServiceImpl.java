package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.domain.UserAuthDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.UserAuthDTO;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.mapper.UserAuthMapper;
import com.hdh.lifeup.service.UserAuthService;
import com.hdh.lifeup.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * UserAuthServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/22
 */
@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private UserAuthMapper userAuthMapper;

    private UserInfoService userInfoService;

    private RedisTemplate redisTemplate;

    @Autowired
    public UserAuthServiceImpl(UserAuthMapper userAuthMapper, UserInfoService userInfoService, RedisTemplate redisTemplate) {
        this.userAuthMapper = userAuthMapper;
        this.userInfoService = userInfoService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserAuthDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public <T> List<UserAuthDTO> listByConditions(T queryCondition) {
        return null;
    }

    @Override
    public <T> PageDTO<UserAuthDTO> pageByConditions(T queryCondition, int currPage, int pageSize) {
        return null;
    }

    @Override
    public UserAuthDTO insert(UserAuthDTO dto) {
        return null;
    }

    @Override
    public UserAuthDTO update(UserAuthDTO dto) {
        return null;
    }

    @Override
    public UserAuthDTO deleteLogically(Long aLong) {
        return null;
    }

    @Override
    public UserAuthDTO delete(Long aLong) {
        return null;
    }

    @Override
    @Transactional
    public String login(UserAuthDTO userAuthDTO, UserInfoDTO userInfoDTO) {
        Preconditions.checkNotNull(userAuthDTO, "【授权登陆】传入的UserAuthDTO为空");
        Preconditions.checkNotNull(userInfoDTO, "【授权登陆】传入的UserInfoDTO为空");

        // authType和authIdentifier查user_auth，取userId
        UserAuthDO userAuthDO = userAuthMapper.selectOne(
            new QueryWrapper<UserAuthDO>().eq("auth_type", userAuthDTO.getAuthType())
                    .eq("auth_identifier", userAuthDTO.getAuthIdentifier())
        );

        // userId 非空 就查user_info，取user信息，userInfoDTO放到缓存，返回token
        UserInfoDTO userInfoResult;
        if (userAuthDO != null) {
            userInfoResult = userInfoService.getOne(userAuthDO.getUserId());
        } else {
            // userId 为空 插入userInfoDTO到user_info
            userInfoDTO.getAuthTypes().add(userAuthDTO.getAuthType());
            userInfoResult = userInfoService.insert(userInfoDTO);

            // 取生成的userInfoDTO.getUserId，set到userAuthDTO并存到user_auth
            userAuthDTO.setUserId(userInfoResult.getUserId());
            insert(userAuthDTO);
        }
        // userInfoDTO放到缓存，返回token TODO


        return UUID.randomUUID().toString();
    }
}
