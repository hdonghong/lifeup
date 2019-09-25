package com.hdh.lifeup.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeamMemberMapperTest {

    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Test
    public void getMembers() throws Exception {
    }

    @Test
    public void getTeamIdsByUserId() throws Exception {
        List<Long> teamIdList = teamMemberMapper.getTeamIdsByUserId(1033360879819161601L);
        Assert.assertEquals("",teamIdList.size() >= 0);
    }

}