package com.hdh.lifeup.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * ApiLimiting anntation<br/>
 * 鉴权+接口限流注解
 * @author hdonghong
 * @since 2018/08/23
 */
@Retention(RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface ApiLimiting {

    /** 规定时间内该账号最多可访问的指定接口的次数 */
    int maxAccess() default 20;

    /** 规定时间 */
    int seconds() default 10;

    /** 是否需要鉴权，默认需要 */
    boolean toAuth() default true;
}
