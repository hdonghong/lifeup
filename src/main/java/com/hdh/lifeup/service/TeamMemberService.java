package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.RecordDTO;
import com.hdh.lifeup.dto.TeamMemberDTO;
import com.hdh.lifeup.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.vo.UserListVO;
import lombok.NonNull;

import java.util.Collection;

/**
 * TeamMemberService interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/11
 */
public interface TeamMemberService extends BaseService<TeamMemberDTO, Long> {

    /**
     * 获取团队某个成员
     * @param teamId 团队id
     * @param userId 成员id
     * @return 成员
     */
    TeamMemberDTO getOne(@NonNull Long teamId, @NonNull Long userId);

    /**
     * 统计团队成员数量
     * @param teamId 团队id
     * @return 成员数量
     */
    int countMembersByTeamId(Long teamId);

    /**
     * 成员发布动态
     * @param teamMemberRecordDTO 动态
     */
    void addMemberRecord(TeamMemberRecordDTO teamMemberRecordDTO);

    /**
     * 加入新成员
     * @param memberDTO 成员DTO
     * @param memberRecordDTO 成员默认发布的动态
     */
    void addMember(TeamMemberDTO memberDTO, TeamMemberRecordDTO memberRecordDTO);

    /**
     * 获取团队成员列表
     * @param teamId 团队id
     * @param pageDTO 查询条件
     * @return 成员列表
     */
    PageDTO<UserListVO> pageMembers(Long teamId, PageDTO pageDTO);

    /**
     * 获取指定成员动态列表
     * @param teamId 团队id
     * @param pageDTO 查询条件
     * @return 动态列表
     */
    PageDTO<RecordDTO> pageMemberRecords(Long teamId, PageDTO pageDTO);

    /**
     * 查看指定用户的userId
     * @param userId 用户id
     * @return 动态列表
     */
    PageDTO<RecordDTO> pageUserRecords(Long userId, PageDTO pageDTO);

    /**
     * 获取多个用户动态列表
     * @param userIds 用户ids
     * @param pageDTO 查询条件
     * @return 动态列表
     */
    PageDTO<RecordDTO> pageUsersRecords(Collection<Long> userIds, PageDTO pageDTO);

    /**
     * 是否团队成员
     * @param teamId 团队id
     * @param userId 成员id
     * @return 1是 0否
     */
    int isMember(Long teamId, Long userId);

    /**
     * 是否签到了
     * @param teamRecordId 团队签到记录id
     * @param userId 成员id
     * @return 1是 0否
     */
    int hasSignedIn(Long teamRecordId, Long userId);


    /**
     * 计数用户加入的团队
     * @param userId 用户id
     * @return 团队数量
     */
    int countUserTeams(Long userId);

    int getAttributeWeekly(Long userId);
}
