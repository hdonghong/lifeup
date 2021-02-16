package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.model.vo.ActivityVO;
import com.hdh.lifeup.model.vo.NextSignVO;
import com.hdh.lifeup.model.vo.TeamDetailVO;
import com.hdh.lifeup.model.vo.TeamTaskVO;
import lombok.NonNull;

import java.util.List;

/**
 * TeamTaskService interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamTaskService {


    TeamTaskDTO getOne(Long teamId);

    TeamTaskDTO insert(@NonNull TeamTaskDTO teamTaskDTO);

    TeamTaskDTO update(TeamTaskDTO teamTaskDTO);

    /**
     * 新增团队
     * @param teamTaskVO 新增团队提交的信息
     * @return 下一次提醒时间
     */
    NextSignVO addTeam(TeamTaskVO teamTaskVO);

    /**
     * 社区团队分页
     * @param pageDTO 分页查询条件
     * @param teamTitle 模糊团队标题
     * @param rankRule
     * @param startDateFilter 是否过滤超过截止时间，默认是
     * @return 分页
     */
    PageDTO<TeamTaskDTO> page(PageDTO pageDTO, String teamTitle, Integer daysCount,
                              Integer rankRule, Boolean startDateFilter, Integer createSource);

    /**
     * 获取成员加入的团队
     * @param userId 成员id
     * @param pageDTO 条件
     * @param isOwner 是否自己创建的
     * @return 团队列表
     */
    PageDTO<TeamTaskDTO> pageUserTeams(Long userId, PageDTO pageDTO, Integer teamStatus, Boolean isOwner);

    /**
     * 获取团队详情
     * @param teamId 团队id
     * @return 详情VO
     */
    TeamDetailVO getDetail(Long teamId);

    /**
     * 获取下一次签到的信息
     * @param teamId 团队id
     * @return 签到信息
     */
    NextSignVO getNextSign(Long teamId);

    /**
     * 成员加入团队
     * @param teamId 团队id
     * @return 签到信息
     */
    NextSignVO joinTeam(Long teamId);

    /**
     * 团队签到
     * @param teamId 团队id
     * @param activityVO 动态VO
     * @return 下一次签到信息
     */
    NextSignVO signIn(Long teamId, ActivityVO activityVO);

    /**
     * 放弃团队，返回下一次签到信息
     * @param teamId
     * @return
     */
    NextSignVO giveUp(Long teamId);

    /**
     * 获取成员所有下一次要签到的团队信息
     * @param userId 成员id
     * @return 所有下一次要签到的团队信息
     */
    List<NextSignVO> getAllNextSigns(Long userId, List<Long> teamIdList);

    /**
     * 终结团队
     * @param teamId 团队id
     */
    void endTeam(Long teamId);

    /**
     * 获取所有活跃的团队
     * 即 team_rank != 0
     * @return
     */
    List<TeamTaskDTO> getAllActiveTeams();

    /**
     * 增加团队活跃度排序值，e 小于0时则减少
     * @param teamId
     * @param e
     * @return
     */
    int incrTeamRank(Long teamId, int e);
}
