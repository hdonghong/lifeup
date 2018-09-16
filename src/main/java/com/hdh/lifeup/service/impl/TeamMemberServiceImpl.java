package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.TeamMemberDO;
import com.hdh.lifeup.domain.TeamMemberRecordDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.RecordDTO;
import com.hdh.lifeup.dto.TeamMemberDTO;
import com.hdh.lifeup.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.mapper.TeamMemberMapper;
import com.hdh.lifeup.mapper.TeamMemberRecordMapper;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.vo.MembersVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public TeamMemberDTO getOne(@NonNull Long teamId, @NonNull Long userId) {
        TeamMemberDO teamMemberDO = memberMapper.selectOne(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
                                                .eq("user_id", userId)
        );
        TeamMemberDTO teamMemberDTO = BaseDTO.from(teamMemberDO, TeamMemberDTO.class);
        if (teamMemberDTO == null) {
            log.error("【获取团队某个成员】不存在的成员，teamId = [{}], userId = [{}]", teamId, userId);
            throw new GlobalException(CodeMsgEnum.NOT_TEAM_MEMBER);
        }
        return teamMemberDTO;
    }

    @Override
    public TeamMemberDTO getOne(Long aLong) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamMemberDTO insert(TeamMemberDTO teamMemberDTO) {
        TeamMemberDO teamMemberDO = teamMemberDTO.toDO(TeamMemberDO.class);
        Integer result = memberMapper.insert(teamMemberDO);
        if (!Objects.equals(1, result)) {
            log.error("【插入新成员】新增失败, teamMemberDTO = [{}]", teamMemberDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        return teamMemberDTO;
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
    public int countMembersByTeamId(@NonNull Long teamId) {
        Integer memberAmount = memberMapper.selectCount(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
        );
        return Optional.ofNullable(memberAmount).orElse(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(TeamMemberRecordDTO teamMemberRecordDTO) {
        teamMemberRecordDTO.setUserId(UserContext.get().getUserId());
        Integer result = memberRecordMapper.insert(teamMemberRecordDTO.toDO(TeamMemberRecordDO.class));
        if (!Objects.equals(1, result)) {
            log.error("【团队成员发布动态】新增失败, teamMemberRecordDTO = [{}]", teamMemberRecordDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(TeamMemberDTO memberDTO, TeamMemberRecordDTO memberRecordDTO) {
        Long userId = UserContext.get().getUserId();
        memberDTO.setUserId(userId);
        this.insert(memberDTO);
        this.addRecord(memberRecordDTO);
    }

    @Override
    public PageDTO<MembersVO> pageMembers(Long teamId, PageDTO pageDTO) {
        IPage<TeamMemberDO> memberDOPage = memberMapper.selectPage(
                new Page<>(pageDTO.getCurrentPage(), pageDTO.getSize()),
                new QueryWrapper<TeamMemberDO>()
                        .eq("team_id", teamId)
                        .orderByAsc("create_time")
        );
        return PageDTO.createFreely(memberDOPage, MembersVO.class);
    }

    @Override
    public PageDTO<RecordDTO> pageMemberRecords(Long teamId, PageDTO pageDTO) {
        Long currentPage = pageDTO.getCurrentPage();
        // FIXME 没有limit
        Integer count = memberRecordMapper.selectCount(
                new QueryWrapper<TeamMemberRecordDO>().eq("team_id", teamId)
        );
        List<RecordDTO> recordList = Lists.newArrayList();
        if (Optional.ofNullable(count).orElse(0) > 0) {
            pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
            recordList = memberRecordMapper.getMembersRecords(teamId, pageDTO);
        }
        return PageDTO.<RecordDTO>builder()
                .currentPage(currentPage)
                .list(recordList)
                .totalPage((long) Math.ceil((count * 1.0) / pageDTO.getSize()))
                .build();
    }
}
