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
import com.hdh.lifeup.util.LocalDateTimeUtil;
import com.hdh.lifeup.util.sensitive.SensitiveFilter;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.time.*;
import java.util.*;

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

    @Test
    public void testSensitive() {
        // 使用默认单例（加载默认敏感词库）
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        // 向过滤器增加一个词
        filter.put("婚礼上唱春天在哪里");

        // 待过滤的句子
        String sentence = "然后，市长在婚礼上手冲唱春天在哪里。";
        // 进行过滤
        String filted = filter.filter(null, '*');

        // 如果未过滤，则返回输入的String引用
        if(!sentence.equals(filted)){
            // 句子中有敏感词
            System.out.println(filted);
        }
    }

    @Test
    public void testZoneId() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
        System.out.println(now);

        System.out.println();

        for (int i = -12, j = 1; i <= 12; i++, j++) {
            LocalDateTime now1 = LocalDateTime.now(ZoneId.of(i >= 0 ? "+" + i : "" + i));
            System.out.println(i + ": " + now1);
        }

        System.out.println(LocalDateTimeUtil.now("GMT+13"));
    }

    @Test
    public void testIsBefore() {
        LocalDateTime nextEndTime = LocalDateTime.of(2020, 02, 16, 23, 59, 59);
        LocalDateTime now = LocalDateTime.of(2020, 02, 16, 4, 37, 8);
        System.out.println(nextEndTime.isBefore(now));
    }

    @Test
    public void testMinus() {
        LocalDateTime lastTwoWeeksDay = LocalDateTime.now().minusWeeks(2);
        System.out.println(lastTwoWeeksDay);
    }

    @Test
    public void testLeetcode() {

        System.out.println(numDecodings("2001"));
    }

    public int numDecodings(String s) {
        if (s.isEmpty() || s.charAt(0) == '0') return 0;
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        for (int i = 1; i < dp.length; ++i) {
            dp[i] = (s.charAt(i - 1) == '0') ? 0 : dp[i - 1];
            if (i > 1 &&
                    (s.charAt(i - 2) == '1' || (s.charAt(i - 2) == '2' && s.charAt(i - 1) <= '6'))) {
                dp[i] += dp[i - 2];
            }
        }
        return dp[s.length()];
    }

    @Test
    public void testUUID() {
        System.out.println(UUID.randomUUID().toString().length());
        Random random = new Random();
        System.out.println(random.nextInt(16));
    }

    private Pair<LocalDateTime, Counter> timeCounter = null;
    /**
     * 每分钟计数
     */
    private void countPerMinute() {
        try {
            LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
            if (timeCounter == null) {
                timeCounter = Pair.of(now, new Counter());
            }
            // 时间不同，先把上一次的计数打印；再记录初始化计数器
            if (!now.equals(timeCounter.getFirst())) {
                System.out.println("TimeCounter_time=[{}]_count=[{}]" + timeCounter.getFirst() + "_" + timeCounter.getSecond());
                timeCounter = Pair.of(now, new Counter());
            }
            timeCounter.getSecond().incr();
        } catch (Exception e) {
        }
    }

    private static class Counter {
        private int cnt;
        private int incr() {
            return ++cnt;
        }
        @Override
        public String toString() {
            return String.valueOf(cnt);
        }
    }

    @Test
    public void test() {
        countPerMinute();
        countPerMinute();
        countPerMinute();
        countPerMinute();
    }
}
