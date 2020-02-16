package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.RecordDTO;
import com.hdh.lifeup.model.dto.TeamMemberDTO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.model.vo.UserListVO;
import lombok.NonNull;

import java.util.List;

/**
 * TeamMemberService interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/11
 */
public interface TeamMemberService {

    /**
     * 获取团队某个成员
     * @param teamId 团队id
     * @param userId 成员id
     * @return 成员
     */
    TeamMemberDTO getOne(@NonNull Long teamId, @NonNull Long userId);

    TeamMemberDTO insert(TeamMemberDTO teamMemberDTO);

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
     * 获取指定团队动态列表
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
     * @param pageDTO 查询条件
     * @param scope 动态范围
     * @param filter 过滤条件，0无；1过滤默认发的
     * @return 动态列表
     */
    PageDTO<RecordDTO> getMoments(PageDTO pageDTO, int scope, int filter, int createSource);

    /**
     * 是否团队成员
     * @param teamId 团队id
     * @param userId 成员id
     * @return 1是 0否
     */
    int isMember(Long teamId, Long userId);

    /**
     * 判断用户是否签到了
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

    /**
     * 计数指定团队状态下用户加入的团队
     * @param userId
     * @param teamStatus
     * @return
     */
    int countUserTeamsWithStatus(Long userId, Integer teamStatus);

    /**
     * 查看用户过去30天参与的团队数
     * @param userId
     * @return
     */
    int countUserLast30DaysTeams(Long userId);

    /**
     * 查看用户过去30天发表的动态数
     * @param userId
     * @return
     */
    int countUserLast30DaysRecords(Long userId);

    /** 排行榜的临时实现：获取用户的经验值
     * @param userId 用户id
     * @return 用户的总经验值
     */
    int getAttributeWeekly(Long userId);

    /**
     * 当前用户退出团队
     * @param teamId 团队id
     * @return 1退出成功；0失败
     */
    int quitTeam(Long teamId);

    /**
     * 获取指定的1条动态详情
     * @param memberRecordId
     * @return
     */
    TeamMemberRecordDTO getOneMemberRecord(Long memberRecordId);

    /**
     * 删除用户发表的动态
     * @param memberRecordId
     * @param userId
     */
    void delUserRecord(Long memberRecordId, Long userId);

    /**
     * 获取指定user参与的团队id
     */
    List<Long> getTeamIdsByUserId(Long userId);
}
