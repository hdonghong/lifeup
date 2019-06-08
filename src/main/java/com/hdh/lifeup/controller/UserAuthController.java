package com.hdh.lifeup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hdh.lifeup.config.AppConfig;
import com.hdh.lifeup.config.YbConfig;
import com.hdh.lifeup.model.constant.AuthTypeConst;
import com.hdh.lifeup.model.dto.UserAuthDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.service.UserAuthService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.MobVO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.model.vo.UserAuthVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

/**
 * AuthController class<br/>
 * @author hdonghong
 * @since 2018/08/20
 */
@Slf4j
@Api(description = "授权/注册/登录模块")
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private YbConfig ybConfig;
    private RestTemplateBuilder restTemplateBuilder;
    private ObjectMapper objectMapper;
    private UserAuthService userAuthService;
    private AppConfig appConfig;

    private static final String ACCESS_TOKEN = "access_token";


    @Autowired
    public UserAuthController(YbConfig ybConfig,
                              RestTemplateBuilder restTemplateBuilder,
                              ObjectMapper objectMapper,
                              UserAuthService userAuthService,
                              AppConfig appConfig) {
        this.ybConfig = ybConfig;
        this.restTemplateBuilder = restTemplateBuilder;
        this.objectMapper = objectMapper;
        this.userAuthService = userAuthService;
        this.appConfig = appConfig;
    }

    @ApiOperation(value = "获取请求第三方授权的URL", notes = "支持多种授权，比如易班、QQ")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authType", value = "授权类型，比如yb、qq", required = true, paramType = "path"),
        @ApiImplicitParam(name = "redirectUri", value = "回调的URI，格式形如：http://lifeup/，不传的话默认为http://net.sarasarasa.lifeup/redirect", paramType = "query")
    })
    @GetMapping("/{authType}")
    public ResultVO<String> getOauthPath(@PathVariable("authType")String authType, String redirectUri) {
        String callback;
        String originalOauthPath;
        String appId;

        switch (authType) {
            case AuthTypeConst.YB: callback = ybConfig.getRedirectUri();
                                   originalOauthPath = ybConfig.getOauthPath();
                                   appId = ybConfig.getAppId();
                                   break;
            default: throw new GlobalException(CodeMsgEnum.UNSUPPORTED_AUTH_TYPE);
        }
        callback = Optional.ofNullable(redirectUri).orElse(callback);
        String oauthPath = String.format(originalOauthPath, appId, callback);

        return Result.success(oauthPath);
    }

    @ApiOperation(value = "易班登录", notes = "拿到code后务必发送给后端")
    @ApiImplicitParam(name = "code", value = "用于拉取access_token", required = true, paramType = "query", dataType = "String")
    @PostMapping("/yb/login")
    public ResultVO<String> ybLogin(@RequestParam("code") String code) throws IOException {
        String accessTokenPath = String.format(ybConfig.getTokenPath(),
                code, ybConfig.getAppId(), ybConfig.getAppSecret(), ybConfig.getRedirectUri());

        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(accessTokenPath, HttpMethod.GET, null, String.class);
        String accessToken = objectMapper.readTree(responseEntity.getBody())
                                         .get(ACCESS_TOKEN).asText();

        responseEntity = restTemplate.exchange(ybConfig.getUserInfoPath() + accessToken, HttpMethod.GET, null, String.class);
        JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
        if (!"success".equals(responseBody.get("status").asText())) {
            log.error("【易班授权】获取登录信息失败，responseBody = [{}]", responseBody);
            throw new GlobalException(CodeMsgEnum.YB_ERROR);
        }

        JsonNode userInfoJson = responseBody.get("info");
        UserAuthDTO userAuthDTO = UserAuthDTO.fromYbUser(userInfoJson);
        UserInfoDTO userInfoDTO = UserInfoDTO.fromYbUser(userInfoJson);

        String token = userAuthService.oauthLogin(userAuthDTO, userInfoDTO);
        return Result.success(token);
    }

    @ApiOperation(value = " QQ登录")
    @PostMapping({"/qq/login"})
    public ResultVO<String> qqLogin(@RequestBody @Valid UserAuthVO userAuthVO) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userAuthVO, userInfoDTO);
        // 注册类型：目前有手机号、QQ
        userInfoDTO.setAuthTypes(Lists.newArrayList(userAuthVO.getAuthType()));

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        // 取生成的userInfoDTO.getUserId，set到userAuthDTO并存到user_auth
        userAuthDTO.setAuthType(AuthTypeConst.QQ)
                   .setAuthIdentifier(userAuthVO.getAuthIdentifier());

        return  Result.success(userAuthService.oauthLogin(userAuthDTO, userInfoDTO));
    }

    @ApiOperation(value = " google登录")
    @PostMapping({"/google/login"})
    public ResultVO<String> googleLogin(@RequestBody @Valid UserAuthVO userAuthVO) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userAuthVO, userInfoDTO);
        // 注册类型：google
        userInfoDTO.setAuthTypes(Lists.newArrayList(userAuthVO.getAuthType()));

        UserAuthDTO userAuthDTO = new UserAuthDTO();
        // 取生成的userInfoDTO.getUserId，set到userAuthDTO并存到user_auth
        userAuthDTO.setAuthType(AuthTypeConst.GOOGLE)
                   .setAuthIdentifier(userAuthVO.getAuthIdentifier());

        return  Result.success(userAuthService.oauthLogin(userAuthDTO, userInfoDTO));
    }

    @ApiOperation(value = "手机号+密码登录")
    @PostMapping("/phone/login")
    public ResultVO<String> phoneLogin(@RequestBody @Valid UserAuthDTO userAuthDTO) {
        userAuthDTO.setAuthType(AuthTypeConst.PHONE);
        return Result.success(userAuthService.appLogin(userAuthDTO));
    }

    @ApiOperation(value = "手机号+验证码登录")
    @PostMapping("/code/login")
    public ResultVO<String> verifyLogin(@RequestBody /*@Valid */MobVO mobVO) {
        // 封装POST请求
        mobVO.setAppKey(appConfig.getAppKey());

/*        // 发起请求
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                appConfig.getMobApi(), mobVO, String.class
        );

        // 验证响应
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return Result.success(userAuthService.codeLogin(
                    new UserAuthDTO().setAuthType(AuthTypeConst.PHONE)
                                     .setAuthIdentifier(mobVO.getPhone())
            ));
        } else {
            // 验证失败
            log.error("【Mob短信登录验证】验证失败，responseEntity = [{}]", responseEntity);
            throw new GlobalException(CodeMsgEnum.MOB_VERIFY_ERROR);
        }*/
        return Result.success(userAuthService.codeLogin(
                new UserAuthDTO().setAuthType(AuthTypeConst.PHONE)
                        .setAuthIdentifier(mobVO.getPhone())
        ));
    }


    @ApiOperation(value = " 注册系统账号", notes = "传递手机号，密码需要客户端md5一下再传，不需要的字段别传")
    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody @Valid UserAuthVO userAuthVO) {
        return Result.success(userAuthService.register(userAuthVO));
    }

}
