package com.hdh.lifeup.model.enums;

import lombok.Getter;

/**
 * LanguageEnum enum<br/>
 *
 * @author hdonghong
 * @since 2020/08/15
 */
@Getter
public enum LanguageEnum {

    SIMPLIFIED_CHINESE("zh"),

    TRADITIONAL_CHINESE("zh-tw"),

    ENGLISH("en"),

    TURKISH("tr"),

    JAPANESE("ja"),
    ;

    private final String value;
    LanguageEnum(String language) {
        this.value = language;
    }

    public static String toValue(String value) {
        for (LanguageEnum e : LanguageEnum.values()) {
            if (e.getValue().equals(value)) {
                return e.getValue();
            }
        }
        return ENGLISH.getValue();
    }
}
