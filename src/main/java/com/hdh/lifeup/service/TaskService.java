package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.TaskDTO;
import lombok.NonNull;

/**
 * TaskService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
public interface TaskService {

    TaskDTO getOne(Long taskId);

    void insert(@NonNull TaskDTO taskDTO);

    void update(@NonNull TaskDTO taskDTO);

    void deleteLogically(@NonNull Long taskId);
}
