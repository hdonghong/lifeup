package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hdh.lifeup.model.constant.AuthTypeConst;
import com.hdh.lifeup.model.domain.UserAuthDO;
import com.hdh.lifeup.model.dto.UserAuthDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.dao.UserAuthMapper;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import com.hdh.lifeup.service.UserAuthService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.PasswordUtil;
import com.hdh.lifeup.util.TokenUtil;
import com.hdh.lifeup.model.vo.UserAuthVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

import static com.hdh.lifeup.model.enums.CodeMsgEnum.TOKEN_INVALID;
import static com.hdh.lifeup.model.enums.CodeMsgEnum.UNSUPPORTED_AUTH_TYPE;

/**
 * UserAuthServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/22
 */
@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private RedisOperator redisOperator;
    private UserAuthMapper userAuthMapper;
    private UserInfoService userInfoService;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserAuthServiceImpl(RedisOperator redisOperator,
                               UserAuthMapper userAuthMapper,
                               UserInfoService userInfoService) {
        this.redisOperator = redisOperator;
        this.userAuthMapper = userAuthMapper;
        this.userInfoService = userInfoService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAuthDTO insert(UserAuthDTO userAuthDTO) {
        Preconditions.checkNotNull(userAuthDTO, "【新增用户鉴权记录】UserAuthDTO类不能为空");
        UserAuthDO userAuthDO = userAuthDTO.toDO(UserAuthDO.class);

        // 新增前检查是否已经注册过
        UserAuthDO queryResult = userAuthMapper.selectOne(
                new QueryWrapper<UserAuthDO>().eq("auth_type", userAuthDO.getAuthType())
                        .eq("auth_identifier", userAuthDO.getAuthIdentifier())
        );
        if (queryResult != null) {
            log.error("【新增用户授权】该授权类型对应的唯一标识已经被注册过，UserAuthDO = [{}]", queryResult);
            throw new GlobalException(CodeMsgEnum.ACCOUNT_ALREADY_EXISTED);
        }

        Integer result = userAuthMapper.insert(userAuthDO);
        if (!Objects.equals(1, result)) {
            log.error("【新增用户授权】插入记录数量 = [{}], UserAuthDTO = [{}]", result, userAuthDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        userAuthDTO.setAuthId(userAuthDO.getAuthId());
        return userAuthDTO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String oauthLogin(UserAuthDTO userAuthDTO, UserInfoDTO userInfoDTO) {
        Preconditions.checkNotNull(userAuthDTO, "【授权登陆】传入的UserAuthDTO为空");
        Preconditions.checkNotNull(userInfoDTO, "【授权登陆】传入的UserInfoDTO为空");

        // 校验accessToken
        this.thirdPlatformAccessToken(userAuthDTO);

        // authType和authIdentifier查user_auth，取userId
        UserAuthDO userAuthDO = userAuthMapper.selectOne(
            new QueryWrapper<UserAuthDO>().eq("auth_type", userAuthDTO.getAuthType())
                    .eq("auth_identifier", userAuthDTO.getAuthIdentifier())
        );

        // userId 非空，用户登录
        UserInfoDTO userInfoResult;
        if (userAuthDO != null) {
            userInfoResult = userInfoService.getOne(userAuthDO.getUserId());
            return this.generateToken(userInfoResult);
        }

        // userId 为空，注册新用户，后再登录
        userInfoDTO.setAuthTypes(Lists.newArrayList(userAuthDTO.getAuthType()));
        if (StringUtils.isEmpty(userInfoDTO.getNickname())) {
            userInfoDTO.setNickname(userAuthDTO.getAuthType() + "_" + System.currentTimeMillis());
        }
        userInfoResult = userInfoService.insert(userInfoDTO);
        userAuthDTO.setUserId(userInfoResult.getUserId());
        insert(userAuthDTO);

        // 返回token
        return this.generateToken(userInfoResult);
    }

    @Override
    public String appLogin(@NonNull UserAuthDTO userAuthDTO) {
        // authType和authIdentifier查user_auth，取userId
        UserAuthDO userAuthDO = userAuthMapper.selectOne(
                new QueryWrapper<UserAuthDO>().eq("auth_type", userAuthDTO.getAuthType())
                        .eq("auth_identifier", userAuthDTO.getAuthIdentifier())
        );
        if (userAuthDO == null) {
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        UserInfoDTO userInfoResult = userInfoService.getOne(userAuthDO.getUserId());
        boolean match = PasswordUtil.checkPwd(
                    userAuthDTO.getAccessToken(), userInfoResult.getPwdSalt(), userAuthDO.getAccessToken());
        if (!match) {
            log.error("【APP账号登录】密码错误");
            throw new GlobalException(CodeMsgEnum.PASSWORD_ERROR);
        }
        // 返回token
        return this.generateToken(userInfoResult);
    }

    @Override
    public String codeLogin(@NonNull UserAuthDTO userAuthDTO) {
        // authType和authIdentifier查user_auth，取userId
        UserAuthDO userAuthDO = userAuthMapper.selectOne(
                new QueryWrapper<UserAuthDO>().eq("auth_type", userAuthDTO.getAuthType())
                        .eq("auth_identifier", userAuthDTO.getAuthIdentifier())
        );
        if (userAuthDO == null) {
            throw new GlobalException(CodeMsgEnum.USER_NOT_EXIST);
        }
        UserInfoDTO userInfoResult = userInfoService.getOne(userAuthDO.getUserId());
        // 返回token
        return this.generateToken(userInfoResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(@NonNull UserAuthVO userAuthVO) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userAuthVO, userInfoDTO);
        // 注册类型：目前有手机号、QQ
        userInfoDTO.setAuthTypes(Lists.newArrayList(userAuthVO.getAuthType()));
        UserInfoDTO userInfoResult = userInfoService.insert(userInfoDTO);

        log.info("【注册账号】clientPwd = [{}]", userAuthVO.getAccessToken());

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        // 取生成的userInfoDTO.getUserId，set到userAuthDTO并存到user_auth
        userAuthDTO.setAuthType(userAuthVO.getAuthType())
                   .setUserId(userInfoResult.getUserId())
                   .setAuthIdentifier(userAuthVO.getAuthIdentifier());
        if (AuthTypeConst.PHONE.equals(userAuthVO.getAuthType())) {
            userAuthDTO.setAccessToken(PasswordUtil.convertClientPwdToDbPwd(userAuthVO.getAccessToken(), userInfoResult.getPwdSalt()));
        }
        insert(userAuthDTO);
        // 返回token
        return this.generateToken(userInfoResult);
    }

    /**
     * 用户信息userInfoDTO放到缓存，返回token
     * @param userInfoDTO 用户信息
     * @return token
     */
    private String generateToken(UserInfoDTO userInfoDTO) {
        String token = TokenUtil.get();
        redisOperator.setex(UserKey.TOKEN, token, userInfoDTO);
        return token;
    }

    private void thirdPlatformAccessToken(UserAuthDTO authDTO) {
        if (AuthTypeConst.WEIBO.equals(authDTO.getAuthType())) {
            String checkPath = "https://api.weibo.com/oauth2/get_token_info?access_token=" + authDTO.getAccessToken();
            RestTemplate restTemplate = restTemplateBuilder.build();
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(checkPath, HttpMethod.POST, null, String.class);
                String appkey = objectMapper.readTree(responseEntity.getBody()).get("appkey").asText();
                String uid = objectMapper.readTree(responseEntity.getBody()).get("uid").asText();
                long expireIn = objectMapper.readTree(responseEntity.getBody()).get("expire_in").asLong();
                if (!Objects.equals("3682612064", appkey) || !Objects.equals(authDTO.getAuthIdentifier(), uid) || expireIn < 0) {
                    throw new GlobalException(TOKEN_INVALID);
                }
                return;
            } catch (IOException ignored) {
            } catch (HttpClientErrorException e) {
                throw new GlobalException(TOKEN_INVALID);
            }
        } else if (AuthTypeConst.QQ.equals(authDTO.getAuthType())) {
            String oauthUrl = "https://graph.qq.com/user/get_user_info?access_token=$access_token$&openid=$openid$&oauth_consumer_key=101492659";
            oauthUrl = oauthUrl.replace("$access_token$", authDTO.getAccessToken())
                .replace("$openid$", authDTO.getAuthIdentifier());
            RestTemplate restTemplate = restTemplateBuilder.build();
            ResponseEntity<String> responseEntity = restTemplate.exchange(oauthUrl, HttpMethod.GET, null, String.class);
            try {
                String ret = objectMapper.readTree(responseEntity.getBody()).get("ret").asText();
                if (!Objects.equals("0", ret)) {
                    throw new GlobalException(TOKEN_INVALID);
                }
            } catch (IOException ignored) {
            }
            return;
        } else if (AuthTypeConst.GOOGLE.equals(authDTO.getAuthType())) {
            String oauthUrl = "http://178.128.26.36:8080/tokenInfo?id_token=$access_token$";
            if (StringUtils.isEmpty(authDTO.getAccessToken())) {
                return;
            }
            oauthUrl = oauthUrl.replace("$access_token$", authDTO.getAccessToken());
            RestTemplate restTemplate = restTemplateBuilder.build();
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(oauthUrl, HttpMethod.GET, null, String.class);
                String data = objectMapper.readTree(responseEntity.getBody()).get("data").asText();
                if (!StringUtils.isEmpty(data) && data.contains("error")) {
                    throw new GlobalException(TOKEN_INVALID);
                }
                // body中返回值=400的情况也做限制 TODO
            } catch (GlobalException e) {
              throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else if (AuthTypeConst.YB.equals(authDTO.getAuthType())) {
            return;
        }
        throw new GlobalException(UNSUPPORTED_AUTH_TYPE);
    }
}
