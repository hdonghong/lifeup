package com.hdh.lifeup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * YbConfig class<br/>
 *
 * @author hdonghong
 * @since 2018/08/20
 */
@Data
@ConfigurationProperties(prefix = "yb")
@Component
public class YbConfig {

    private String appId;

    private String appSecret;

    private String redirectUri;

    private String oauthPath;

    private String tokenPath;

    private String userInfoPath;
}
