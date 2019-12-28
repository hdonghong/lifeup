package com.hdh.lifeup;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdh.lifeup.dao.TeamMemberRecordMapper;
import com.hdh.lifeup.dao.TeamTaskMapper;
import com.hdh.lifeup.model.domain.TeamMemberRecordDO;
import com.hdh.lifeup.model.domain.TeamTaskDO;
import com.hdh.lifeup.model.vo.TeamActivityRankVO;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.service.TeamTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author  hdonghong
 * @since 2018/07/15
 */
@Slf4j
@Controller
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class LifeupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeupApplication.class, args);
	}

	@RequestMapping(value = {"/", "index", "index.html"})
	public String home() {
		return "redirect:http://lifeup.hdonghong.top/index.html";
	}


	@RequestMapping(value = {"/sdfasdfasdfasdfasdfasdfasdfsdf"})
	public String doTempTask() {
        tempTask();
		return "ok";
	}

    @Resource
    private TeamMemberService teamMemberService;
    @Resource
    private TeamTaskMapper teamTaskMapper;
    @Resource
    private TeamMemberRecordMapper teamMemberRecordMapper;
    @Resource
    private TeamTaskService teamTaskService;

    public void tempTask() {
        log.info("[任务] 初始化团队活跃度开始执行, thread = {}", Thread.currentThread().getId());
        int offset = 1;
        while (true) {
            IPage<TeamTaskDO> teamTaskDOPage = teamTaskMapper.selectPage(
                    new Page<TeamTaskDO>().setCurrent(offset++).setSize(500),
                    new QueryWrapper<>()
            );
            List<TeamTaskDO> teamTaskList = teamTaskDOPage.getRecords();
            if (teamTaskList.size() == 0) {
                break;
            }
            teamTaskList.forEach(teamTask -> {

                Long teamId = teamTask.getTeamId();
                // 根据 team_id 查 team _member 表求团队成员数 * 1
                int teamMemberCount = teamMemberService.countMembersByTeamId(teamId);

                // 根据 team_id 查 team_member_record 表求团队成员动态数 * 4
                Integer activityCount = teamMemberRecordMapper.selectCount(
                        new QueryWrapper<TeamMemberRecordDO>().eq("team_id", teamId));

                // 根据 user_id 查 team_member 表求过去 30 天创建者参与团队数 * 1
                Long userId = teamTask.getUserId();
                int ownerTeamCount = teamMemberService.countUserLast30DaysTeams(userId);

                // 根据 user_id 查 team_member_record 表求过去 30 天创建者发表动态数 * 2
                int ownerActivityCount = teamMemberService.countUserLast30DaysRecords(userId);

                // 计算出 team_rank 值更新到 team_task 表
                TeamActivityRankVO teamActivityRankVO = new TeamActivityRankVO()
                        .setActivityCount(activityCount)
                        .setTeamMemberCount(teamMemberCount)
                        .setOwnerTeamCount(ownerTeamCount)
                        .setOwnerActivityCount(ownerActivityCount);
                teamTaskService.incrTeamRank(teamId, teamActivityRankVO.getTeamRank());
            });
            log.info("[任务] 初始化团队活跃度进行中... offset = {}, size = {}", offset - 1, teamTaskList.size());
        }
        log.info("[任务] 初始化团队活跃度执行完毕， offset = {}, thread = {}", offset, Thread.currentThread().getId());
    }
}
