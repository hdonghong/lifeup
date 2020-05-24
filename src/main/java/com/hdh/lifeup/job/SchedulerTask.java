package com.hdh.lifeup.job;

import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.service.TeamTaskService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.service.UserRankService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    @Resource
    private UserInfoService userInfoService;
    @Resource
    private UserRankService userRankService;

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

    @Scheduled(cron="*0 0 3 * * ?")
    public void updateUserRank() {
        int current = 1;
        int limit = 500;

        while (true) {
            System.err.println("start updateUserRank");
            List<UserInfoDTO> userInfoDTOList = userInfoService.listUser(current, limit);
            if (CollectionUtils.isEmpty(userInfoDTOList)) {
                break;
            }
            userInfoDTOList.forEach(userInfoDTO ->
                    userRankService.updateRankValue(userInfoDTO.getUserId()));
            current++;
        }
    }
}
