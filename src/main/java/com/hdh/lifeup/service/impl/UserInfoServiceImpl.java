package com.hdh.lifeup.service.impl;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.auth.TokenContext;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.domain.UserInfoDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.mapper.UserInfoMapper;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.PasswordUtil;
import com.hdh.lifeup.util.TokenUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * UserInfoServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private RedisTemplate<String, UserInfoDTO> redisTemplate;

    private UserInfoMapper userInfoMapper;

    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoDTO getOne(@NonNull Long userId) {
        UserInfoDO userInfoDO = userInfoMapper.selectById(userId);
        if (userInfoDO == null) {
            log.error("【获取用户】不存在的用户，userId = [{}]", userId);
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        return UserInfoDTO.from(userInfoDO, UserInfoDTO.class);
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
    public UserInfoDTO insert(@NonNull UserInfoDTO userInfoDTO) {
        UserInfoDO userInfoDO = userInfoDTO.toDO(UserInfoDO.class);
        userInfoDO.setPwdSalt(PasswordUtil.getSalt());
        Integer result = userInfoMapper.insert(userInfoDO);
        if (!Objects.equal(1, result)) {
            log.error("【新增用户信息】插入记录数量 = [{}], UserInfoDTO = [{}]", result, userInfoDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        return userInfoDTO.setUserId(userInfoDO.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO update(@NonNull UserInfoDTO userInfoDTO) {
        UserInfoDTO storedUserInfoDTO = UserContext.get();
        BeanUtils.copyProperties(userInfoDTO, storedUserInfoDTO, "user_id", "createTime");
        Integer result = userInfoMapper.updateById(storedUserInfoDTO.toDO(UserInfoDO.class));
        if (!Objects.equal(1, result)) {
            log.error("【修改用户信息】插入记录数量 = [{}], UserInfoDTO = [{}]", result, userInfoDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        redisTemplate.opsForValue().set(TokenContext.get(), storedUserInfoDTO, TokenUtil.EXPIRED_SECONDS, TimeUnit.SECONDS);
        return storedUserInfoDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO deleteLogically(
            @NotNull(message = "【删除用户】用户id不能为空") Long userId) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO delete(
            @NotNull(message = "【删除用户】用户id不能为空") Long userId) {
        return null;
    }

    @Override
    public UserInfoDTO getByToken(String authenticityToken) {
        Preconditions.checkNotNull(authenticityToken, "【通过token获取用户】传入的Token为空");

        UserInfoDTO userInfoDTO = redisTemplate.opsForValue().get(authenticityToken);
        if (userInfoDTO == null) {
            log.info("【通过token获取用户】无效的TOKEN");
            return null;
        }

        Long expire = redisTemplate.boundValueOps(authenticityToken).getExpire();
        if (Optional.ofNullable(expire).orElse(0L) < TokenUtil.MIN_EXPIRED) {
            log.info("【通过token获取用户】当前用户Token有效时长expire = [{}], 重设", expire);
            redisTemplate.opsForValue().set(authenticityToken, userInfoDTO, TokenUtil.EXPIRED_SECONDS, TimeUnit.SECONDS);
        }

        return userInfoDTO;
    }
}
