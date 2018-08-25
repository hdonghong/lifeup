package com.hdh.lifeup.util;


import com.google.common.base.Objects;
import lombok.NonNull;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * PasswordUtil class<br/>
 * 密码的工具类，包括生成盐、客户端传来的密码转成DB存储的密码、密码校验
 * @author hdonghong
 * @since 2018/08/24
 */
public class PasswordUtil {

    public static String getSalt() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String convertClientPwdToDbPwd(@NonNull String clientPwd, @NonNull String salt) {
        clientPwd = salt.indexOf(0) + salt.indexOf(1) + clientPwd + salt.indexOf(salt.length() - 1) + salt.indexOf(salt.length() - 2);
        return DigestUtils.md5DigestAsHex(clientPwd.getBytes());
    }

    public static boolean checkPwd(@NonNull String clientPwd, @NonNull String salt, @NonNull String dbPwd) {
        return Objects.equal(dbPwd, convertClientPwdToDbPwd(clientPwd, salt));
    }

    public static void main(String[] args) {
        System.out.println(convertClientPwdToDbPwd("sss", "salt").length());
    }
}
