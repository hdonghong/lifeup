package com.hdh.lifeup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.TeamMemberRecordDO;
import com.hdh.lifeup.model.domain.TeamRecordDO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.model.vo.NextSignVO;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.time.*;
import java.util.Date;
import java.util.List;
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

    @Test
    public void testJackson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> authTypes = Lists.newArrayList("qq", "phone");
        String str = objectMapper.writeValueAsString(authTypes);
        System.out.println(str);

        List list = objectMapper.readValue(str, List.class);
        System.out.println(list);
    }

    @Test
    public void testClass() {
        String clazzName3 = new Object() {
            private String getClassName() {
                String clazzName = this.getClass().getName();
                return clazzName.substring(0, clazzName.lastIndexOf('$'));
            }
        }.getClassName();
        try {
            Class<?> aClass = Class.forName(clazzName3);
            OtherTest o = (OtherTest) aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDate() {
        System.out.println(new Date());
    }

    @Test
    public void testBeanUtils() {
        NextSignVO nextSignVO = new NextSignVO().setTeamTitle("aaa").setRewardExp(1);
        System.out.println(nextSignVO);

        TeamRecordDO teamRecordDO = new TeamRecordDO().setNextStartTime(LocalDateTime.now());
        BeanUtils.copyProperties(teamRecordDO, nextSignVO);
        System.out.println(nextSignVO);
    }

    @Test
    public void testSublist() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        int size = 3;
        System.out.println(list.subList(0, 0 + size));
        // 1, 2, 3

        System.out.println(list.subList(1, 1 + size));
        // 2, 3, 4

        System.out.println(list.subList(3, 3 + size));
        // 4, 5, 6
    }

    @Test
    public void testMemberRecordDTO() {
        TeamMemberRecordDO memberRecordDO = new TeamMemberRecordDO()
                .setActivityImages("[\"http://lifeup.hdonghong.top/images/activities/289a7a87-ab0a-4b80-9daf-d45f4f20cefe.jpg\"]");
        TeamMemberRecordDTO from = BaseDTO.from(memberRecordDO, TeamMemberRecordDTO.class);
        System.out.println(from);
    }
}
