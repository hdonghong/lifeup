package com.hdh.lifeup.service.impl;

import com.google.common.collect.Lists;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.service.TeamMemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeamMemberServiceImplTest {

    @Autowired
    private TeamMemberService teamMemberService;

    @Test
    public void pageUsersRecords() throws Exception {
        PageDTO<Object> pageDTO = PageDTO.builder().size(10L).totalPage(1L).currentPage(1L).build();
        List<Long> coll = Lists.newArrayList(1033007682448465921L, 1033280752341962754L, 1040252416820748289L);
//        PageDTO<RecordDTO> page = teamMemberService.pageUsersRecords(coll, pageDTO);
//        System.out.println(page);
    }

}