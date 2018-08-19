package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.domain.UserInfoDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.mapper.UserInfoMapper;
import com.hdh.lifeup.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserInfoServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private UserInfoMapper userInfoMapper;

    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoDTO getOne(Long userId) {
        if (userId == null) {
            log.error("【获取用户】用户id为空");
            throw new GlobalException(CodeMsgEnum.PARAMETER_NULL);
        }
        UserInfoDO userInfoDO = userInfoMapper.selectById(userId);
        if (userInfoDO == null) {
            log.error("【获取用户】不存在的用户，userId = [{}]", userId);
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userInfoDO, userInfoDTO);
        return userInfoDTO;
    }

    @Override
    public <T> List<UserInfoDTO> listByConditions(T queryCondition) {
        return null;
    }

    @Override
    public <T> PageDTO<UserInfoDTO> pageByConditions(T queryCondition, int currPage, int pageSize) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO insert(UserInfoDTO userInfoDTO) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO update(UserInfoDTO userInfoDTO) {
        if (userInfoDTO == null) {
            log.error("【新增/修改用户】用户DTO为空");
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO deleteLogically(Long userId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO delete(Long userId) {
        return null;
    }
}
