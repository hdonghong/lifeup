package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.dao.TaskMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.domain.TaskDO;
import com.hdh.lifeup.model.dto.TaskDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.service.TaskService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            log.warn("【获取个人事项】taskId为空");
            throw new GlobalException(CodeMsgEnum.PARAMETER_NULL);
        }
        TaskDO taskDO = taskMapper.selectById(taskId);
        if (taskDO == null) {
            log.warn("【获取个人事项】不存在的个人事项，taskId = [{}]", taskId);
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }
        return BaseDTO.from(taskDO, TaskDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(@NonNull TaskDTO taskDTO) {
        taskDTO.setUserId(UserContext.get().getUserId());
        TaskDO taskDO = taskDTO.toDO(TaskDO.class);
        Integer result = taskMapper.insert(taskDO);
        if (Objects.equals(1, result)) {
            log.warn("【新增个人事项】新增失败，taskDTO = [{}]", taskDTO);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
        taskDTO.setTaskId(taskDO.getTaskId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull TaskDTO taskDTO) {
        if (taskDTO.getTaskId() == null) {
            log.warn("【更新个人事项】");
            throw new GlobalException(CodeMsgEnum.PARAMETER_NULL);
        }
        taskDTO.setUserId(UserContext.get().getUserId());
        Integer result = taskMapper.update(
                taskDTO.toDO(TaskDO.class),
                new QueryWrapper<TaskDO>().eq("task_id", taskDTO.getTaskId())
                                          .eq("user_id", taskDTO.getUserId())
        );
        if (Objects.equals(1, result)) {
            log.warn("【删除个人事项】不存在的个人事项，taskDTO = [{}]", taskDTO);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLogically(@NonNull Long taskId) {
        Integer result = taskMapper.delete(
                new QueryWrapper<TaskDO>().eq("task_id", taskId)
                        .eq("user_id", UserContext.get().getUserId())
        );
        if (Objects.equals(1, result)) {
            log.warn("【删除个人事项】不存在的个人事项，taskId = [{}]", taskId);
            throw new GlobalException(CodeMsgEnum.TASK_NOT_EXIST);
        }
    }
}
