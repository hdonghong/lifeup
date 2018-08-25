package com.hdh.lifeup.util;

import java.util.UUID;

/**
 * TokenUtil class<br/>
 * Token工具类，鉴权
 * @author hdonghong
 * @since 2018/08/23
 */
public class TokenUtil {

    /** Token鉴权，模仿自github */
    public static final String AUTHENTICITY_TOKEN = "AUTHENTICITY_TOKEN";

    /** 有效期 */
    public static final int EXPIRED_SECONDS = 3600;

    /** 最小有效期，当小于时仍继续访问就为token增加有效时间 */
    public static final int MIN_EXPIRED = 600;

    public static String get() {
        return UUID.randomUUID().toString();
    }
}
