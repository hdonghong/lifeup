package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TeamTaskDTO;
import com.hdh.lifeup.vo.NextSignVO;
import com.hdh.lifeup.vo.TeamDetailVO;
import com.hdh.lifeup.vo.TeamTaskVO;

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
}
