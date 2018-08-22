package com.hdh.lifeup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AppPropertiesConfig class<br/>
 * 系统应用的常量属性配置
 * @author hdonghong
 * @since 2018/08/20
 */
@Data
@ConfigurationProperties(prefix = "app")
@Component
public class AppConfig {

    private String path;

}
