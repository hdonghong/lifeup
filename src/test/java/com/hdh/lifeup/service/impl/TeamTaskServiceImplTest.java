package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TeamTaskDTO;
import com.hdh.lifeup.service.TeamTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamTaskServiceImplTest {

    @Autowired
    private TeamTaskService teamTaskService;

    @Test
    public void page() throws Exception {
        PageDTO<TeamTaskDTO> page = teamTaskService.page(
                PageDTO.builder().currentPage(1L).size(10L).build()
        );
        System.out.println(page);
    }

}