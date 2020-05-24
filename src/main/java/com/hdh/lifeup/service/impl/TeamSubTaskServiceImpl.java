package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.TeamSubTaskMapper;
import com.hdh.lifeup.model.domain.TeamSubTaskDO;
import com.hdh.lifeup.model.dto.TeamSubTaskDTO;
import com.hdh.lifeup.service.TeamSubTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TeamSubTaskServiceImpl class<br/>
 * 团队子任务
 * @author hdonghong
 * @since 2020/05/18
 */
@Slf4j
@Service
public class TeamSubTaskServiceImpl implements TeamSubTaskService {

    @Resource
    private TeamSubTaskMapper teamSubTaskMapper;

    @Override
    public void batchInsert(Long teamId, List<TeamSubTaskDTO> teamSubTaskDTOList) {
        if (CollectionUtils.isEmpty(teamSubTaskDTOList)) {
            return;
        }
        teamSubTaskDTOList.stream()
                .map(teamSubTaskDTO -> {
                    teamSubTaskDTO.setTeamId(teamId);
                    return teamSubTaskDTO.toDO(TeamSubTaskDO.class);
                })
                .forEach(teamSubTaskMapper::insert);
    }

    @Override
    public List<TeamSubTaskDTO> listByTeamId(Long teamId) {
        QueryWrapper<TeamSubTaskDO> queryWrapper = new QueryWrapper<TeamSubTaskDO>()
                .eq("team_id", teamId);
        return teamSubTaskMapper.selectList(queryWrapper)
                .stream()
                .map(teamSubTaskDO -> TeamSubTaskDTO.from(teamSubTaskDO, TeamSubTaskDTO.class))
                .collect(Collectors.toList());
    }
}
