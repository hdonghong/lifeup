package com.hdh.lifeup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.lifeup.config.YbConfig;
import com.hdh.lifeup.constant.AuthTypeConst;
import com.hdh.lifeup.dto.UserAuthDTO;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.service.UserAuthService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

/**
 * AuthController class<br/>
 * @author hdonghong
 * @since 2018/08/20
 */
@Slf4j
@Api(description = "用户授权登录模块，包含第三方应用登录和本应用登录")
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private YbConfig ybConfig;

    private RestTemplateBuilder restTemplateBuilder;

    private ObjectMapper objectMapper;

    private UserAuthService userAuthService;

    private static final String ACCESS_TOKEN = "access_token";

    @Autowired
    public UserAuthController(YbConfig ybConfig,
                              RestTemplateBuilder restTemplateBuilder,
                              ObjectMapper objectMapper,
                              UserAuthService userAuthService) {
        this.ybConfig = ybConfig;
        this.restTemplateBuilder = restTemplateBuilder;
        this.objectMapper = objectMapper;
        this.userAuthService = userAuthService;
    }

    @ApiOperation(value = "获取请求第三方授权的URL", notes = "支持多种授权，比如易班、QQ")
    @ApiImplicitParam(name = "redirectUri", value = "回调的URI，格式形如：http://lifeup/，不传的话默认为http://net.sarasarasa.lifeup/redirect", required = false, paramType = "query", dataType = "String")
    @GetMapping("/{authType}")
    public ResultVO<String> getOauthPath(@PathVariable("authType")String authType, String redirectUri) {
        String callback;
        String originalOauthPath;
        String appId;

        switch (authType) {
            case AuthTypeConst.YB: callback = ybConfig.getRedirectUri();
                                   originalOauthPath = ybConfig.getOauthPath();
                                   appId = ybConfig.getOauthPath();
                                   break;
            case AuthTypeConst.QQ:
            default: throw new GlobalException(CodeMsgEnum.UNSUPPORTED_AUTH_TYPE);
        }
        callback = Optional.ofNullable(redirectUri).orElse(callback);
        String oauthPath = String.format(originalOauthPath, appId, callback);

        return Result.success(oauthPath);
    }

    @ApiOperation(value = "发送code", notes = "拿到code后务必发送给后端")
    @ApiImplicitParam(name = "code", value = "用于拉取access_token", required = true, paramType = "query", dataType = "String")
    @PostMapping("/yb/login")
    public ResultVO<?> ybLogin(@RequestParam("code") String code) throws IOException {
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
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }

        JsonNode userInfoJson = responseBody.get("info");
        UserAuthDTO userAuthDTO = UserAuthDTO.fromYbUser(userInfoJson);
        UserInfoDTO userInfoDTO = UserInfoDTO.fromYbUser(userInfoJson);

        String token = userAuthService.login(userAuthDTO, userInfoDTO);
        return Result.success(token);
    }


}
