package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.TeamSubTaskDTO;

import java.util.List;

/**
 * TeamSubTaskService interface<br/>
 *
 * @author hdonghong
 * @since 2020/05/18
 */
public interface TeamSubTaskService {

    void batchInsert(Long teamId, List<TeamSubTaskDTO> teamSubTaskDTOList);

    List<TeamSubTaskDTO> listByTeamId(Long teamId);
}
