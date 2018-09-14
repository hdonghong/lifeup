package com.hdh.lifeup.enums;

import lombok.Getter;

/**
 * CodeMsgEnum enum<br/>
 * 与客户端交互的状态码信息类
 *
 * @author hdonghong
 * @since 2018/08/10
 */
@Getter
public enum CodeMsgEnum {

    /** 通用模块状态码  */
    SUCCESS(200, "success"),
    SERVER_ERROR(500100, "服务端异常错误"),
    PARAMETER_ERROR(500101, "请求参数校验错误"),
    PARAMETER_NULL(500102, "传递了空值"),
    DATABASE_EXCEPTION(500103, "数据库crud操作异常"),

    /** 授权登录模块 */
    UNSUPPORTED_AUTH_TYPE(500201, "不支持的授权类型"),
    TOKEN_ABSENT(500202, "请求头中缺少鉴权TOKEN"),
    TOKEN_INVALID(500203, "不合法的Token或者Token失效"),
    ACCESS_ILLEGAL(500204, "非法访问他人资源"),
    ACCOUNT_ALREADY_EXISTED(500205, "账号已被注册"),
    PASSWORD_ERROR(500206, "密码错误"),

    /** 用户模块 5003XX */
    USER_NOT_EXIST(500301, "用户不存在"),

    /** 事项模块 5004XX */
    TASK_NOT_EXIST(500401, "个人事项不存在"),

    /** 属性模块 5005XX */
    ATTRIBUTE_NOT_EXIST(500501, "人物属性不存在"),

    /** 团队模块 5006XX */
    TEAM_NOT_EXIST(500601, "团队不存在"),

    ;

    private int code;
    private String msg;

    CodeMsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
