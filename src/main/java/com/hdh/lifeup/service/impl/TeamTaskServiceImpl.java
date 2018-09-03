package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.mapper.TeamTaskMapper;
import com.hdh.lifeup.service.TeamTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * TeamTaskServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/09/03
 */
@Slf4j
@Service
public class TeamTaskServiceImpl implements TeamTaskService {

    private TeamTaskMapper teamTaskMapper;

    @Autowired
    public TeamTaskServiceImpl(TeamTaskMapper teamTaskMapper) {
        this.teamTaskMapper = teamTaskMapper;
    }

    @Override
    public BaseDTO getOne(Serializable serializable) {
        return null;
    }

    @Override
    public BaseDTO insert(BaseDTO dto) {
        return null;
    }

    @Override
    public BaseDTO update(BaseDTO dto) {
        return null;
    }

    @Override
    public BaseDTO deleteLogically(Serializable serializable) {
        return null;
    }

    @Override
    public BaseDTO delete(Serializable serializable) {
        return null;
    }

    @Override
    public PageDTO pageByConditions(Object queryCondition, int currPage, int pageSize) {
        return null;
    }

    @Override
    public List listByConditions(Object queryCondition) {
        return null;
    }
}
