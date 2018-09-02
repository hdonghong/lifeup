package com.hdh.lifeup.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * VersionVO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Data
@ConfigurationProperties(prefix = "app.version")
@Component
public class VersionVO {

    /** 示例：1 */
    private Integer newVersion;

    /** 最新版本链接 */
    private String downloadUrl;

    /** 示例：v1.0 */
    private String versionName;
}
