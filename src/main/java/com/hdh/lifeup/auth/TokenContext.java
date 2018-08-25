package com.hdh.lifeup.auth;

/**
 * TokenContext class<br/>
 *
 * @author hdonghong
 * @since 2018/08/24
 */
public class TokenContext {

    private static ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void set(String authenticityToken) {
        tokenHolder.set(authenticityToken);
    }

    public static String get() {
        return tokenHolder.get();
    }

    public static void remove() {
        tokenHolder.remove();
    }
}
