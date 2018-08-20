package com.hdh.lifeup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.time.*;
import java.util.Map;

/**
 * OtherTest class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
public class OtherTest {

    @Test
    public void testTotalPage() {
        int total = 11;
        int size = 10;
        Long totalPage = new Long((long) Math.ceil((total * 1.0) / size));
        System.out.println(totalPage);
    }

    @Test
    public void testLocalDateAndTime() throws JsonProcessingException {
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = nowDateTime.toLocalDate();
        LocalTime nowTime = nowDateTime.toLocalTime();

        System.out.println("nowDateTime: " + nowDateTime.toEpochSecond(ZoneOffset.of("+8")));
        System.out.println("Date + Time: " + LocalDateTime.of(nowDate, nowTime).toEpochSecond(ZoneOffset.of("+8")));
        System.out.println("nowDate: " + nowDate.toEpochDay());
        System.out.println("nowTime: " + nowTime.toSecondOfDay());

        System.out.println("jackson-------------------------------");
        Map<String, Object> map = Maps.newHashMap();
        map.put("nowDateTime", nowDateTime);
        map.put("nowDate", nowDate);
        map.put("nowTime", nowTime);

        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(map);
        System.out.println(str);
        System.out.println("instant-------------------------------");

        Instant instant = nowDateTime.toInstant(ZoneOffset.MAX);
        System.out.println("Max:" + instant);
        instant = nowDateTime.toInstant(ZoneOffset.MIN);
        System.out.println("Min:" + instant);
        instant = nowDateTime.toInstant(ZoneOffset.UTC);
        System.out.println("UTC:" + instant);
    }
}
