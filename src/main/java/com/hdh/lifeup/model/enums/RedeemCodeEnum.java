package com.hdh.lifeup.model.enums;

import lombok.Getter;

/**
 * RedeemCodeEnum enum<br/>
 * 兑换码状态枚举
 * @author hdonghong
 * @since 2020/04/06
 */
@Getter
public enum RedeemCodeEnum {

    INIT(0, "初始状态，未发放"),

    GIVE_OUT(1, "已发放"),

    REDEEMED(2, "已兑换")
    ;

    private Integer status;
    private String desc;

    RedeemCodeEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
