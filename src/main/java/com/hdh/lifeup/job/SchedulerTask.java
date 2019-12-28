package com.hdh.lifeup.job;

import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.service.TeamTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * SchedulerTask class<br/>
 * 定时任务类
 * @author hdonghong
 * @since 2019/12/08
 */
@Component
public class SchedulerTask {

    @Resource
    private TeamTaskService teamTaskService;

    /**
     * 团队活跃度缩减机制
     */
    @Scheduled(cron="*0 0 5 * * ?")
    public void decrTeamRank() {
        List<TeamTaskDTO> teamTaskList = teamTaskService.getAllActiveTeams();
        teamTaskList.forEach(teamTask -> {
            // 每次至少减 1
            int decrement = (int) -Math.max(teamTask.getTeamRank() * 0.05, 1);
            teamTaskService.incrTeamRank(teamTask.getTeamId(), decrement);
        });
    }

}
