package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.dao.TeamMemberMapper;
import com.hdh.lifeup.dao.TeamMemberRecordMapper;
import com.hdh.lifeup.dao.TeamTaskMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.domain.TeamMemberDO;
import com.hdh.lifeup.model.domain.TeamMemberRecordDO;
import com.hdh.lifeup.model.domain.TeamTaskDO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.RecordDTO;
import com.hdh.lifeup.model.dto.TeamMemberDTO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.UserListVO;
import com.hdh.lifeup.redis.LikeKey;
import com.hdh.lifeup.redis.MemberRecordKey;
import com.hdh.lifeup.redis.RedisOperator;
import com.hdh.lifeup.redis.UserKey;
import com.hdh.lifeup.service.AsyncTaskService;
import com.hdh.lifeup.service.LikeService;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.service.UserInfoService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import static com.hdh.lifeup.model.constant.BizTypeConst.TEAM_MEMBER_RECORD;
import static com.hdh.lifeup.model.constant.CommonConst.*;
import static com.hdh.lifeup.model.constant.UserConst.FollowStatus;
import static com.hdh.lifeup.model.enums.ActionEnum.ADD_ACTIVITY;
import static com.hdh.lifeup.model.enums.ActionEnum.BROWSE_ACTIVITY;

/**
 * TeamMemberServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/09/11
 */
@Slf4j
@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    @Autowired
    private TeamMemberMapper memberMapper;

    @Autowired
    private TeamMemberRecordMapper memberRecordMapper;

    @Autowired
    private RedisOperator redisOperator;

    // FIXME
    @Autowired
    private TeamTaskMapper teamTaskMapper;

    @Autowired
    private LikeService likeService;

    @Resource
    private AsyncTaskService asyncTaskService;

    @Resource
    private UserInfoService userInfoService;

    @Override
    public TeamMemberDTO getOne(@NonNull Long teamId, @NonNull Long userId) {
        TeamMemberDO teamMemberDO = memberMapper.selectOne(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
                                                .eq("user_id", userId)
        );
        TeamMemberDTO teamMemberDTO = BaseDTO.from(teamMemberDO, TeamMemberDTO.class);
        if (teamMemberDTO == null) {
            log.warn("【获取团队某个成员】不存在的成员，teamId = [{}], userId = [{}]", teamId, userId);
            throw new GlobalException(CodeMsgEnum.MEMBER_NOT_IN_TEAM);
        }
        return teamMemberDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamMemberDTO insert(TeamMemberDTO teamMemberDTO) {
        TeamMemberDO teamMemberDO = teamMemberDTO.toDO(TeamMemberDO.class);
        Integer result = memberMapper.insert(teamMemberDO);
        if (!Objects.equals(1, result)) {
            log.warn("【插入新成员】新增失败, teamMemberDTO = [{}]", teamMemberDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        return teamMemberDTO;
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
    public void addMemberRecord(TeamMemberRecordDTO teamMemberRecordDTO) {
        Long memberUserId = UserContext.get().getUserId();
        // 用户退出团队后 重新加入不再发加入的动态。
        if (ActivityIcon.IC_JOIN.equals(teamMemberRecordDTO.getActivityIcon())) {
            Integer count = memberRecordMapper.selectCount(
                    new QueryWrapper<TeamMemberRecordDO>()
                            .eq("user_id", memberUserId)
                            .eq("team_id", teamMemberRecordDTO.getTeamId())
            );
            if (!Objects.equals(0, count)) {
                return;
            }
        }
        teamMemberRecordDTO.setUserId(memberUserId);
        TeamMemberRecordDO teamMemberRecordDO = teamMemberRecordDTO.toDO(TeamMemberRecordDO.class);
        Integer result = memberRecordMapper.insert(teamMemberRecordDO);
        if (!Objects.equals(1, result)) {
            log.warn("【团队成员发布动态】新增失败, teamMemberRecordDTO = [{}]", teamMemberRecordDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        teamMemberRecordDTO.setMemberRecordId(teamMemberRecordDO.getMemberRecordId());
        // 异步更新团队活跃度
        asyncTaskService.updateTeamRank(
                teamMemberRecordDTO.getTeamId(), teamMemberRecordDTO.getUserId(), teamMemberRecordDTO.getActivityIcon());
        asyncTaskService.reportAction(UserContext.get().getUserId(), ADD_ACTIVITY, teamMemberRecordDO.getMemberRecordId(), TEAM_MEMBER_RECORD);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMember(TeamMemberDTO memberDTO, TeamMemberRecordDTO memberRecordDTO) {
        if (this.isMember(memberDTO.getTeamId(), memberDTO.getUserId()) != 0) {
            log.warn("【加入团队】禁止重复加入团队，memberDTO = [{}]", memberDTO);
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }

        this.insert(memberDTO);
        this.addMemberRecord(memberRecordDTO);
    }

    @Override
    public PageDTO<UserListVO> pageMembers(Long teamId, PageDTO pageDTO) {
        Long currentPage = pageDTO.getCurrentPage();
        // FIXME 没有limit
        Integer count = memberMapper.selectCount(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
        );
        List<UserListVO> membersList = Lists.newArrayList();
        if (Optional.ofNullable(count).orElse(0) > 0) {
            pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
            membersList = memberMapper.getMembers(teamId, pageDTO);
            membersList.forEach(member -> {
                // 默认‘我’没有关注这个member
                int followStatus = FollowStatus.NOT_FOLLOW;
                Long currentUserId = UserContext.get().getUserId();
                Long memberId = member.getUserId();
                if (currentUserId.equals(memberId)) {
                    followStatus = FollowStatus.MYSELF;
                } else if (redisOperator.zrank(UserKey.FOLLOWING, currentUserId, memberId) != null ){
                    // 如果我关注了这个member
                    followStatus = FollowStatus.FOLLOWING;
                    // 如果这个member也关注了我
                    if (redisOperator.zrank(UserKey.FOLLOWING, memberId, currentUserId) != null ) {
                        followStatus = FollowStatus.INTERACTIVE;
                    }
                }
                member.setIsFollow(followStatus);
            });
        }
        return PageDTO.<UserListVO>builder()
                .currentPage(currentPage)
                .list(membersList)
                .totalPage((long) Math.ceil((count * 1.0) / pageDTO.getSize()))
                .build();
    }

    @Override
    public PageDTO<RecordDTO> pageMemberRecords(Long teamId, PageDTO pageDTO) {
        Integer count = memberRecordMapper.selectCount(
                new QueryWrapper<TeamMemberRecordDO>().eq("team_id", teamId)
        );
        long totalPage = (long) Math.ceil((count * 1.0) / pageDTO.getSize());
        Long currentPage = pageDTO.getCurrentPage();

        List<RecordDTO> recordList = Lists.newArrayList();
        if (totalPage >= currentPage) {
            pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
            recordList = memberRecordMapper.getMemberRecords(teamId, pageDTO);
            Long userId = UserContext.get().getUserId();
            assembleRecordList(recordList, userId);
        }
        asyncTaskService.reportAction(UserContext.get().getUserId(), BROWSE_ACTIVITY, teamId, TEAM_MEMBER_RECORD);

        return PageDTO.<RecordDTO>builder()
                .currentPage(currentPage)
                .list(recordList)
                .totalPage(totalPage)
                .build();
    }

    private void assembleRecordList(List<RecordDTO> recordList, Long userId) {
        recordList.forEach(recordDTO -> {
            Long memberRecordId = recordDTO.getMemberRecordId();
            int isLike = likeService.isLike(LikeKey.ACTIVITY, memberRecordId, userId);
            int likeCount = likeService.getRecordLikeCount(memberRecordId);
            Integer userType = userInfoService.getUserType(recordDTO.getUserId());
            recordDTO.setIsLike(isLike)
                    .setLikeCount(likeCount)
                    .setUserType(userType);
        });
    }

    @Override
    public PageDTO<RecordDTO> pageUserRecords(Long userId, PageDTO pageDTO) {
        // 当传入的用户id为空时，返回当前登录的用户信息
        if (userId == null) {
            userId = UserContext.get().getUserId();
        }
        IPage<TeamMemberRecordDO> userRecordsPage = memberRecordMapper.selectPage(
                new Page<>(pageDTO.getCurrentPage(), pageDTO.getSize()),
                new QueryWrapper<TeamMemberRecordDO>()
                        .eq("user_id", userId)
                        .ne("activity_icon", ActivityIcon.IC_GIVE_UP)
                        .orderByDesc("member_record_id")
        );
        PageDTO<RecordDTO> recordPage = PageDTO.createFreely(userRecordsPage, RecordDTO.class);
        assembleRecordList(recordPage.getList(), UserContext.get().getUserId());
        return recordPage;
    }

    @Override
    public PageDTO<RecordDTO> getMoments(PageDTO pageDTO, int scope, int filter, int createSource) {
        Long currentPage = pageDTO.getCurrentPage();
        Integer count;
        long totalPage;
        Long userId = UserContext.get().getUserId();
        List<RecordDTO> recordList = Lists.newArrayList();
        // 如果是指定在圈子内
        if (ActivityScope.MYFOLLOWERS.equals(scope)) {
            Set<Long> userIdSet = redisOperator.zrange(UserKey.FOLLOWING, userId, 0, -1);
            userIdSet.add(userId);
            count = memberRecordMapper.selectCount(
                    new QueryWrapper<TeamMemberRecordDO>()
                            .in("user_id", userIdSet)
                            .ne("activity_icon", ActivityIcon.IC_GIVE_UP)
                            .eq("create_source", createSource)
            );
            totalPage = (long) Math.ceil((count * 1.0) / pageDTO.getSize());
            if (totalPage >= currentPage) {
                pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
                recordList = memberRecordMapper.getRecordsByUserIds(userIdSet, pageDTO, filter, createSource);
            }
        } else {
            // 否则认为指定在所有人
            count = memberRecordMapper.selectCount(
                    new QueryWrapper<TeamMemberRecordDO>()
                            .ne("activity_icon", ActivityIcon.IC_JOIN)
                            .ne("activity_icon", ActivityIcon.IC_GIVE_UP)
                            .eq("create_source", createSource)
            );
            totalPage = (long) Math.ceil((count * 1.0) / pageDTO.getSize());
            if (totalPage >= currentPage) {
                pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
                recordList = memberRecordMapper.getRecords(pageDTO, filter, createSource);
            }
        }
        assembleRecordList(recordList, userId);
        asyncTaskService.reportAction(UserContext.get().getUserId(), BROWSE_ACTIVITY, null, TEAM_MEMBER_RECORD);
        return PageDTO.<RecordDTO>builder()
                      .currentPage(currentPage)
                      .list(recordList)
                      .totalPage((long) Math.ceil((count * 1.0) / pageDTO.getSize()))
                      .build();
    }

    @Override
    public int isMember(Long teamId, Long userId) {
        Integer result = memberMapper.selectCount(
                new QueryWrapper<TeamMemberDO>().eq("team_id", teamId)
                        .eq("user_id", userId)
        );
        return Optional.ofNullable(result).orElse(0);
    }

    @Override
    public int hasSignedIn(Long teamRecordId, Long userId) {
        Integer result = memberRecordMapper.selectCount(
                new QueryWrapper<TeamMemberRecordDO>().eq("team_record_id", teamRecordId)
                        .eq("user_id", userId)
        );
        return Optional.ofNullable(result).orElse(0);
    }

    @Override
    public int countUserTeams(Long userId) {
        Integer countResult = memberMapper.selectCount(
                new QueryWrapper<TeamMemberDO>().eq("user_id", userId)
        );
        return Optional.ofNullable(countResult).orElse(0);
    }

    @Override
    public int countUserTeamsWithStatus(Long userId, Integer teamStatus) {
        if (teamStatus == null) {
            return countUserTeams(userId);
        }

        return teamTaskMapper.countUserTeamsWithStatus(userId, teamStatus);
    }

    @Override
    public int countUserLast30DaysTeams(Long userId) {
        return memberMapper.countUserLast30DaysTeams(userId);
    }

    @Override
    public int countUserLast30DaysRecords(Long userId) {
        return memberRecordMapper.countUserLast30DaysRecords(userId);
    }


    @Override
    // TODO
    public int getAttributeWeekly(Long userId) {
        Long attributeWeek = redisOperator.get(UserKey.ATTRIBUTE_WEEK, userId);
        if (attributeWeek != null) {
            return attributeWeek.intValue();
        }
        TemporalField fieldISO = WeekFields.of(Locale.CHINA).dayOfWeek();
//        LocalDateTime monday = LocalDateTime.of(LocalDate.now().with(fieldISO, 1), LocalTime.MIN);
        LocalDateTime lastSomeWeeksDay = LocalDateTime.now().minusWeeks(4);
//        LocalDateTime sunday = LocalDateTime.of(LocalDate.now().with(fieldISO, 7), LocalTime.create(23, 59, 59, 0));
        List<TeamMemberRecordDO> memberRecordDOList = memberRecordMapper.selectList(
                new QueryWrapper<TeamMemberRecordDO>().eq("user_id", userId)
                                                      .gt("create_time", lastSomeWeeksDay)
                                                      .lt("create_time", LocalDateTime.now().withNano(0))
        );
        List<Long> teamIdList = memberRecordDOList.stream().map(TeamMemberRecordDO::getTeamId).collect(Collectors.toList());
        if (teamIdList.size() == 0) {
            return 0;
        }
        Integer expSum = teamTaskMapper.selectBatchIds(teamIdList)
                .stream().map(TeamTaskDO::getRewardExp)
                .reduce(0, (sum, e) -> sum + e);
        redisOperator.setex(UserKey.ATTRIBUTE_WEEK, userId, expSum);
        return expSum;
    }

    @Override
    public int quitTeam(Long teamId) {
        Integer result = memberMapper.delete(
                new QueryWrapper<TeamMemberDO>().eq("user_id", UserContext.get().getUserId())
                        .eq("team_id", teamId)
                        .eq("team_role", TeamRole.MEMBER)
        );
        if (Optional.ofNullable(result).orElse(0) == 0) {
            log.warn("【退出团队】失败，teamId = [{}], user = [{}]", teamId, UserContext.get());
        }
        return result;
    }

    @Override
    public TeamMemberRecordDTO getOneMemberRecord(Long memberRecordId) {
        TeamMemberRecordDTO teamMemberRecordDTO = redisOperator.get(MemberRecordKey.ID, memberRecordId);
        if (teamMemberRecordDTO != null) {
            return teamMemberRecordDTO;
        }

        TeamMemberRecordDO memberRecordDO = memberRecordMapper.selectById(memberRecordId);
        if (memberRecordDO == null) {
            log.warn("【获取成员记录】不存在，memberRecordId = [{}]", memberRecordId);
            throw new GlobalException(CodeMsgEnum.MEMBER_RECORD_NOT_EXIT);
        }
        teamMemberRecordDTO = BaseDTO.from(memberRecordDO, TeamMemberRecordDTO.class);
        redisOperator.set(MemberRecordKey.ID, memberRecordId, teamMemberRecordDTO);

        return teamMemberRecordDTO;
    }

    @Override
    public void delUserRecord(Long memberRecordId, Long userId) {
        Integer ret = memberRecordMapper.delete(
                new QueryWrapper<TeamMemberRecordDO>()
                .eq("member_record_id", memberRecordId)
                .eq("user_id", userId)
        );
        if (!Objects.equals(ret, 1)) {
            log.warn("【删除动态】不存在的动态，memberRecordId = [{}], userId = [{}]", memberRecordId, userId);
            throw new GlobalException(CodeMsgEnum.MEMBER_RECORD_NOT_EXIT);
        }
    }

    @Override
    public List<Long> getTeamIdsByUserId(Long userId) {
        return memberMapper.getTeamIdsByUserId(userId);
    }
}
