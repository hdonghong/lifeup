package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TaskRecordDTO;
import com.hdh.lifeup.mapper.TaskRecordMapper;
import com.hdh.lifeup.service.TaskRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TaskRecordServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Slf4j
@Service
public class TaskRecordServiceImpl implements TaskRecordService {

    private TaskRecordMapper taskRecordMapper;

    @Autowired
    public TaskRecordServiceImpl(TaskRecordMapper taskRecordMapper) {
        this.taskRecordMapper = taskRecordMapper;
    }

    @Override
    public TaskRecordDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public TaskRecordDTO insert(TaskRecordDTO dto) {
        return null;
    }

    @Override
    public TaskRecordDTO update(TaskRecordDTO dto) {
        return null;
    }

    @Override
    public TaskRecordDTO deleteLogically(Long aLong) {
        return null;
    }

    @Override
    public TaskRecordDTO delete(Long aLong) {
        return null;
    }
}
