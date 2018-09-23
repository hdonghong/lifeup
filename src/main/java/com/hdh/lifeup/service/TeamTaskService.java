package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TeamTaskDTO;
import com.hdh.lifeup.vo.ActivityVO;
import com.hdh.lifeup.vo.NextSignVO;
import com.hdh.lifeup.vo.TeamDetailVO;
import com.hdh.lifeup.vo.TeamTaskVO;

import java.util.List;

/**
 * TeamTaskService interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamTaskService extends BaseService<TeamTaskDTO, Long> {

    /**
     * 新增团队
     * @param teamTaskVO 新增团队提交的信息
     * @return 下一次提醒时间
     */
    NextSignVO addTeam(TeamTaskVO teamTaskVO);

    PageDTO<TeamTaskDTO> page(PageDTO pageDTO);

    /**
     * 获取成员加入的团队
     * @param userId 成员id
     * @param pageDTO 条件
     * @return 团队列表
     */
    PageDTO<TeamTaskDTO> pageUserTeams(Long userId, PageDTO pageDTO);

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
     * 获取成员所有下一次要签到的团队信息
     * @param memberId 成员id
     * @return 所有下一次要签到的团队信息
     */
    List<NextSignVO> getAllNextSigns(Long memberId);

}
