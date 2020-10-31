package com.hdh.lifeup.model.enums;

import lombok.Getter;

/**
 * CodeLevelEnum class<br/>
 *
 * @author hdonghong
 * @since 2020/10/31
 */
@Getter
public enum CodeLevelEnum {

    ADMIN(-1),

    GENERAL(0),

    VIP(1),
    ;

    private final Integer level;
    CodeLevelEnum(Integer level) {
        this.level = level;
    }
}
