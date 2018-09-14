package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.TeamMemberDTO;

/**
 * TeamMemberService interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/11
 */
public interface TeamMemberService extends BaseService<TeamMemberDTO, Long> {

    int countByTeamId(Long teamId);
}
