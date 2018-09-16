package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.domain.TaskDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TaskDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.mapper.TaskMapper;
import com.hdh.lifeup.service.TaskService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * TaskServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO getOne(Long taskId) {
        if (taskId == null) {
            log.error("【获取个人事项】taskId为空");
            throw new GlobalException(CodeMsgEnum.PARAMETER_NULL);
        }
        TaskDO taskDO = taskMapper.selectById(taskId);
        if (taskDO == null) {
            log.error("【获取个人事项】不存在的个人事项，taskId = [{}]", taskId);
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(taskDO, taskDTO);
        return taskDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDTO insert(@NonNull TaskDTO taskDTO) {
        taskDTO.setUserId(UserContext.get().getUserId());
        TaskDO taskDO = taskDTO.toDO(TaskDO.class);
        Integer result = taskMapper.insert(taskDO);
        if (Objects.equals(1, result)) {
            log.error("【新增个人事项】新增失败，taskDTO = [{}]", taskDTO);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
        taskDTO.setTaskId(taskDO.getTaskId());
        return taskDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDTO update(@NonNull TaskDTO taskDTO) {
        if (taskDTO.getTaskId() == null) {
            log.error("【更新个人事项】");
            throw new GlobalException(CodeMsgEnum.PARAMETER_NULL);
        }
        taskDTO.setUserId(UserContext.get().getUserId());
        Integer result = taskMapper.update(
                taskDTO.toDO(TaskDO.class),
                new QueryWrapper<TaskDO>().eq("task_id", taskDTO.getTaskId())
                                          .eq("user_id", taskDTO.getUserId())
        );
        if (Objects.equals(1, result)) {
            log.error("【删除个人事项】不存在的个人事项，taskDTO = [{}]", taskDTO);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
        return taskDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDTO deleteLogically(@NonNull Long taskId) {
        Integer result = taskMapper.delete(
                new QueryWrapper<TaskDO>().eq("task_id", taskId)
                        .eq("user_id", UserContext.get().getUserId())
        );
        if (Objects.equals(1, result)) {
            log.error("【删除个人事项】不存在的个人事项，taskId = [{}]", taskId);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskDTO delete(Long taskId) {
        return null;
    }
}
