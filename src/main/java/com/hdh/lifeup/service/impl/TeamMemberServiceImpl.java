package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.domain.TeamMemberDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TeamMemberDTO;
import com.hdh.lifeup.mapper.TeamMemberMapper;
import com.hdh.lifeup.mapper.TeamMemberRecordMapper;
import com.hdh.lifeup.service.TeamMemberService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TeamMemberServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/09/11
 */
@Slf4j
@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    private TeamMemberMapper memberMapper;
    private TeamMemberRecordMapper memberRecordMapper;

    @Autowired
    public TeamMemberServiceImpl(TeamMemberMapper memberMapper,
                                 TeamMemberRecordMapper memberRecordMapper) {
        this.memberMapper = memberMapper;
        this.memberRecordMapper = memberRecordMapper;
    }

    @Override
    public TeamMemberDTO getOne(Long aLong) {
        return null;
    }

    @Override
    public <T> List<TeamMemberDTO> listByConditions(T queryCondition) {
        return null;
    }

    @Override
    public <T> PageDTO<TeamMemberDTO> pageByConditions(T queryCondition, int currPage, int pageSize) {
        return null;
    }

    @Override
    public TeamMemberDTO insert(TeamMemberDTO dto) {

        return null;
    }

    @Override
    public TeamMemberDTO update(TeamMemberDTO dto) {
        return null;
    }

    @Override
    public TeamMemberDTO deleteLogically(Long aLong) {
        return null;
    }

    @Override
    public TeamMemberDTO delete(Long aLong) {
        return null;
    }

    @Override
    public int countByTeamId(@NonNull Long teamId) {
        Integer memberAmount = memberMapper.selectCount(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
        );
        return Optional.ofNullable(memberAmount).orElse(0);
    }
}
