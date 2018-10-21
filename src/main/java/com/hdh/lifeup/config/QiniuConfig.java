package com.hdh.lifeup.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * QiniuConfig class<br/>
 *
 * @author hdonghong
 * @since 2018/10/16
 */
@Data
@ConfigurationProperties(prefix = "qiniu")
@Component
public class QiniuConfig {

    private String cdnPath;
    private String accessKey;
    private String secretKey;
    private String bucket;

    public static final String AVATAR_URI = "images/avatars/";
    public static final String ACTIVITY_URI = "images/activities/";
    public static final String UNKNOWNS_URI = "images/unknowns/";

    public static String getImageURI(String imageCategory) {
        switch (imageCategory) {
            case "avatar" : return AVATAR_URI;
            case "activity" : return ACTIVITY_URI;
            default:return UNKNOWNS_URI;
        }
    }
}
