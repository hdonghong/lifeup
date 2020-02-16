package com.hdh.lifeup.model.enums;

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
    SUCCESS(200, "success", "success"),
    SERVER_ERROR(500100, "服务端异常错误", "system error"),
    PARAMETER_ERROR(500101, "请求参数校验错误", "parameter illegal"),
    PARAMETER_NULL(500102, "传递了空值", "parameter is empty"),
    DATABASE_EXCEPTION(500103, "数据库crud操作异常", "database exception"),

    /** 授权登录模块 */
    UNSUPPORTED_AUTH_TYPE(500201, "不支持的授权类型", "unsupported auth type"),
    TOKEN_ABSENT(500202, "请求头中缺少鉴权TOKEN", "token is absent"),
    TOKEN_INVALID(500203, "不合法的Token或者Token失效", "token illegal"),
    ACCESS_ILLEGAL(500204, "非法访问他人资源", "access illegal"),
    ACCOUNT_ALREADY_EXISTED(500205, "账号已被注册", "account already existed"),
    PASSWORD_ERROR(500206, "密码错误", "password error"),
    YB_ERROR(500207, "易班授权登录失败", "yiban error"),
    MOB_VERIFY_ERROR(500208, "短信验证失败", "mob verify error"),
    TOO_MANY_ACCESSES(500209, "操作次数过多", "too many accesses"),

    /** 用户模块 5003XX */
    USER_NOT_EXIST(500301, "用户不存在", "user not exist"),
    FORBID_FOLLOW_YOURSELF(500302, "用户无法关注自己", "forbid follow yourself"),
    FOLLOW_ERROR(500303, "已关注或者其它异常情况", "follow error"),

    /** 事项模块 5004XX */
    TASK_NOT_EXIST(500401, "个人事项不存在", "task not exist"),

    /** 属性模块 5005XX */
    ATTRIBUTE_NOT_EXIST(500501, "人物属性不存在", "attribute not exist"),

    /** 团队模块 5006XX */
    TEAM_NOT_EXIST(500601, "团队不存在", "team not exist"),
    TEAM_OUT_OF_DATE(500602, "逾期加入团队", "team out of date"),
    TEAM_IS_END(500603, "团队任务已经结束", "team is end"),
    TEAM_NOT_SIGN_TIME(500604, "当前不是签到时间", "team not sign time"),
    TEAM_INVALID_BEHAVIOR(500605, "越权操作", "team invalid behavior"),
    TEAM_INVALID_COIN(500606, "金额越界", "team invalid coin"),

    /** 团队成员模块 5007XX */
    MEMBER_NOT_IN_TEAM(500701, "不是团队成员", "member not in team"),
    MEMBER_RECORD_NOT_EXIT(500702, "成员记录不存在", "member record not exit"),

    /** 点赞 */
    LIKE_ERROR(500801, "点赞操作失败", "like error"),
    LIKE_COUNT_NOT_ENOUGH(500802, "点赞数不足", "like count not enough"),
    ;

    private int code;
    private String msg;
    private String globalMsg;

    CodeMsgEnum(int code, String msg, String globalMsg) {
        this.code = code;
        this.msg = msg;
        this.globalMsg = globalMsg;
    }
}
