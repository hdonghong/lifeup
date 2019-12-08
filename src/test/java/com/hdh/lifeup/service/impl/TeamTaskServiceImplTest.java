package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.vo.ActivityVO;
import com.hdh.lifeup.model.vo.NextSignVO;
import com.hdh.lifeup.model.vo.TeamTaskVO;
import com.hdh.lifeup.service.TeamTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamTaskServiceImplTest {

    @Before
    public void setUp() {
        UserContext.set(new UserInfoDTO().setUserId(1033360879819161601L));
    }

    @Test
    public void addTeam() throws Exception {
        TeamTaskVO teamTaskVO = new TeamTaskVO();
        teamTaskVO.setTeamTitle("1")
                .setTeamDesc("2")
                .setRewardAttrs(new ArrayList<>())
                .setRewardExp(1)
                .setTeamFreq(1)
                .setFirstStartTime(LocalDateTime.of(2019, 11, 21, 13, 0))
                .setFirstEndTime(LocalDateTime.of(2019, 11, 26, 13, 0))
                .setTeamHead("http://baidu.com/4.png")
                .setCoin(1)
                .setCoinVariable(2);


        teamTaskService.addTeam(teamTaskVO);
    }

    @Test
    public void getNextSign() throws Exception {
        NextSignVO nextSign = teamTaskService.getNextSign(1198515109891182593L);
        System.out.println(nextSign);
    }

    @Test
    public void signIn() throws Exception {
        ActivityVO activityVO = new ActivityVO();
        activityVO.setActivity("?");
        NextSignVO nextSignVO = teamTaskService.signIn(1198515109891182593L, activityVO);
        System.out.println(nextSignVO);
    }

    @Autowired
    private TeamTaskService teamTaskService;

    @Test
    public void page() throws Exception {
    }

}