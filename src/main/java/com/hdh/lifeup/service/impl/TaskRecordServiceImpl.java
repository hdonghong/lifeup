package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.dao.TaskRecordMapper;
import com.hdh.lifeup.service.TaskRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
