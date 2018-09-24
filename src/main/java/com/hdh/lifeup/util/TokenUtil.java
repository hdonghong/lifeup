package com.hdh.lifeup.util;

import java.util.UUID;

/**
 * TokenUtil class<br/>
 * Token工具类，鉴权
 * @author hdonghong
 * @since 2018/08/23
 */
public class TokenUtil {

    /** Token鉴权，模仿自github，但由于Nginx默认忽略带下划线的Header，故使用中横线 */
    public static final String AUTHENTICITY_TOKEN = "authenticity-token";

    /** 有效期 */
    public static final int EXPIRE_SECONDS = 3600 * 24 * 3;

    /** 最小有效期，当小于时仍继续访问就为token增加有效时间 */
    public static final int MIN_EXPIRED = 3600;

    public static String get() {
        return UUID.randomUUID().toString();
    }
}
