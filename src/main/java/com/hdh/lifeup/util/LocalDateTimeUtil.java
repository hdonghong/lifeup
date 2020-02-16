package com.hdh.lifeup.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * LocalDateTimeUtil class<br/>
 * 时间工具类
 * @author hdonghong
 * @since 2020/02/15
 */
public class LocalDateTimeUtil {

    /**
     *
     * @param timeZone 可空
     * @return
     */
    public static LocalDateTime now(String timeZone) {
        LocalDateTime localDateTime;
        if (StringUtils.isEmpty(timeZone)) {
            localDateTime = LocalDateTime.now();
        } else {
            localDateTime = LocalDateTime.now(ZoneId.of(timeZone));
        }
        return localDateTime;
    }

    public static LocalDate localDateNow(String timeZone) {
        LocalDate localDate;
        if (StringUtils.isEmpty(timeZone)) {
            localDate = LocalDate.now();
        } else {
            localDate = LocalDate.now(ZoneId.of(timeZone));
        }
        return localDate;
    }
}
