package com.hdh.lifeup.dao;

import com.google.common.collect.Lists;
import com.hdh.lifeup.model.dto.PageDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeamMemberRecordMapperTest {

    @Autowired
    private TeamMemberRecordMapper teamMemberRecordMapper;

    @Test
    public void getRecordsByUserIds() throws Exception {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setCurrentPage(0L);
        pageDTO.setSize(3L);
        List<Long> objects = Lists.newArrayList();
        objects.add(1L);
//        teamMemberRecordMapper.getRecordsByUserIds(objects, pageDTO, 0);
    }

    @Test
    public void getRecords() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setCurrentPage(0L);
        pageDTO.setSize(3L);
//        teamMemberRecordMapper.getRecords(pageDTO, 1);
    }

}