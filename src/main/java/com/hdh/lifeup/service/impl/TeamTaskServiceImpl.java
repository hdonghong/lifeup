package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.TimeZoneContext;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.dao.TeamRecordMapper;
import com.hdh.lifeup.dao.TeamTaskMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.exception.SingleTaskException;
import com.hdh.lifeup.model.domain.TeamRecordDO;
import com.hdh.lifeup.model.domain.TeamTaskDO;
import com.hdh.lifeup.model.dto.*;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.ActivityVO;
import com.hdh.lifeup.model.vo.NextSignVO;
import com.hdh.lifeup.model.vo.TeamDetailVO;
import com.hdh.lifeup.model.vo.TeamTaskVO;
import com.hdh.lifeup.redis.LikeKey;
import com.hdh.lifeup.service.*;
import com.hdh.lifeup.util.LocalDateTimeUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hdh.lifeup.model.constant.BizTypeConst.TEAM_MEMBER_RECORD;
import static com.hdh.lifeup.model.constant.BizTypeConst.TEAM_TASK;
import static com.hdh.lifeup.model.constant.CommonConst.*;
import static com.hdh.lifeup.model.enums.ActionEnum.*;

/**
 * TeamTaskServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/09/03
 */
@Slf4j
@Service
public class TeamTaskServiceImpl implements TeamTaskService {

    private TeamTaskMapper teamTaskMapper;

    private TeamRecordMapper teamRecordMapper;

    private TeamMemberService memberService;

    private UserInfoService userInfoService;

    @Resource
    private TeamSubTaskService teamSubTaskService;

    @Autowired
    private LikeService likeService;
    @Resource
    private AsyncTaskService asyncTaskService;

    @Autowired
    public TeamTaskServiceImpl(TeamTaskMapper teamTaskMapper,
                               TeamRecordMapper teamRecordMapper,
                               TeamMemberService teamMemberService,
                               UserInfoService userInfoService) {
        this.teamTaskMapper = teamTaskMapper;
        this.teamRecordMapper = teamRecordMapper;
        this.memberService = teamMemberService;
        this.userInfoService = userInfoService;
    }

    @Override
    public TeamTaskDTO getOne(Long teamId) {
        Preconditions.checkNotNull(teamId, "[TeamTaskServiceImpl.getOne] teamId is null");
        TeamTaskDO teamTaskDO = teamTaskMapper.selectById(teamId);
        if (teamTaskDO == null) {
            log.error("【TeamTaskServiceImpl.getOne】不存在的团队任务，teamId = [{}]", teamId);
            throw new GlobalException(CodeMsgEnum.TEAM_NOT_EXIST);
        }
        return BaseDTO.from(teamTaskDO, TeamTaskDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TeamTaskDTO insert(@NonNull TeamTaskDTO teamTaskDTO) {
        // 截止加入时间如果没写，就默认是无限期（2118年11月21日才截止）
        if (teamTaskDTO.getStartDate() == null) {
            teamTaskDTO.setStartDate(LocalDate.of(2118, 11, 21));
        }
        // 根据时区获取时间
        LocalDateTime localNow = LocalDateTimeUtil.now(TimeZoneContext.get());
        teamTaskDTO.setLocalTimeZone(TimeZoneContext.get());
        teamTaskDTO.setLocalCreateTime(localNow);
        if (teamTaskDTO.getFirstStartTime() == null) {
            teamTaskDTO.setFirstStartTime(localNow);
        }
        TeamTaskDO teamTaskDO = teamTaskDTO.toDO(TeamTaskDO.class);
        Integer result = teamTaskMapper.insert(teamTaskDO);
        if (!Objects.equals(1   , result)) {
            log.error("【TeamTaskServiceImpl.insert】插入新teamTask记录失败，teamTaskDTO = [{}]", teamTaskDTO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        teamTaskDTO.setTeamId(teamTaskDO.getTeamId());
        asyncTaskService.reportAction(teamTaskDO.getUserId(), CREATE_TEAM, teamTaskDO.getTeamId(), TEAM_TASK);
        return teamTaskDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NextSignVO addTeam(@NonNull TeamTaskVO teamTaskVO) {
        TeamTaskDTO teamTaskDTO = new TeamTaskDTO();
        teamTaskDTO.setTeamStatus(TaskStatus.DOING);
        BeanUtils.copyProperties(teamTaskVO, teamTaskDTO, "firstStartTime", "firstEndTime");

        // 存主团队表
        LocalDateTime firstStartTime = teamTaskVO.getFirstStartTime();
        LocalDateTime firstEndTime = teamTaskVO.getFirstEndTime();
        teamTaskDTO.setFirstStartTime(firstStartTime)
                   .setFirstEndTime(firstEndTime)
                   .setUserId(UserContext.get().getUserId());
        // 如果用户没有设置团队头像，就把用户头像设置为团队头像
        if (StringUtils.isEmpty(teamTaskVO.getTeamHead())) {
            teamTaskDTO.setTeamHead(UserContext.get().getUserHead());
        }
        this.insert(teamTaskDTO);
        Long teamId = teamTaskDTO.getTeamId();
        // 如果有子任务，则保存子任务
        if (!CollectionUtils.isEmpty(teamTaskVO.getSubTaskList())) {
            teamSubTaskService.batchInsert(teamId, teamTaskVO.getSubTaskList());
        }

        // 先获取下一次签到的VO
        NextSignVO nextSign = this.getNextSign(teamTaskDTO);

        // 创建者作为owner写入成员表
        String localTimeZone = TimeZoneContext.get();
        LocalDateTime localNow = LocalDateTimeUtil.now(localTimeZone);
        TeamMemberDTO memberDTO = new TeamMemberDTO().setTeamId(teamId)
                .setTeamRole(TeamRole.OWNER)
                .setLocalTimeZone(localTimeZone)
                .setLocalCreateTime(localNow)
                .setUserId(teamTaskDTO.getUserId());
        // 创建者默认发布第一条团队成员动态，此动态是没有所属的teamRecordId的，用teamId替代了
        TeamMemberRecordDTO memberRecordDTO = new TeamMemberRecordDTO()
                                            .setTeamId(teamId)
                                            .setTeamTitle(teamTaskDTO.getTeamTitle())
                                            .setTeamRecordId(teamId)
                                            .setUserActivity("创建了团队「" +  teamTaskVO.getTeamTitle() +"」")
                                            .setActivityIcon(ActivityIcon.IC_NEW)
                                            .setCreateSource(teamTaskDTO.getCreateSource())
                                            .setLocalTimeZone(localTimeZone)
                                            .setLocalCreateTime(localNow);
        memberService.addMember(memberDTO, memberRecordDTO);

        return nextSign;

    }

    @Override
    public PageDTO<TeamTaskDTO> page(PageDTO pageDTO, String teamTitle, Integer rankRule, Boolean startDateFilter, Integer createSource) {
//        log.info("pageNo = " + pageDTO.getCurrentPage());
        QueryWrapper<TeamTaskDO> wrapper = new QueryWrapper<TeamTaskDO>()
                .ne("team_status", TaskStatus.COMPLETE);
        // 判断是否过滤掉超过截止时间的团队，默认是过滤
        LocalDateTime localNow = LocalDateTimeUtil.now(TimeZoneContext.get());
        ZonedDateTime zdt = localNow.atZone(ZoneId.of(TimeZoneContext.get()));
        Date date = Date.from(zdt.toInstant());
        if (startDateFilter == null || startDateFilter) {
            wrapper.gt("start_date", date);
        }
        if (createSource == null) {
            createSource = CreateSource.CHINA;
        }
        wrapper.eq("create_source", createSource);
        // 处理团队排序规则
        if (RankRule.ACTIVITY_FIRST.equals(rankRule)) {
            wrapper.orderByDesc("team_rank");
        }
        wrapper.orderByDesc("team_id");
        if (!StringUtils.isEmpty(teamTitle)) {
            wrapper = wrapper.like("team_title", "%" + teamTitle + "%");
        }
        IPage<TeamTaskDO> taskDOPage = teamTaskMapper.selectPage(
                new Page<>(pageDTO.getCurrentPage(), pageDTO.getSize()),
                wrapper
        );
        // 设置点赞
        PageDTO<TeamTaskDTO> teamPage = PageDTO.create(taskDOPage, TeamTaskDTO.class);
        List<TeamTaskDTO> teamTaskDTOList = teamPage.getList();
        assembleTeamList(teamTaskDTOList);
        return teamPage;
    }

    @Override
    public PageDTO<TeamTaskDTO> pageUserTeams(Long userId, PageDTO pageDTO, Integer teamStatus, Boolean isOwner) {
        if (userId == null) {
            userId = UserContext.get().getUserId();
        }
        Long currentPage = pageDTO.getCurrentPage();
        // FIXME count时没有带查询条件
        int count = memberService.countUserTeams(userId);
        List<TeamTaskDO> teamTaskDOList = Lists.newArrayList();
        if (count > 0) {
            pageDTO.setCurrentPage((currentPage - 1) * pageDTO.getSize());
            teamTaskDOList = teamTaskMapper.getUserTeams(userId, pageDTO, teamStatus, isOwner);
        }
        List<TeamTaskDTO> teamTaskDTOList = teamTaskDOList.stream().map(teamTaskDO -> TeamTaskDTO.from(teamTaskDO, TeamTaskDTO.class)).collect(Collectors.toList());
        assembleTeamList(teamTaskDTOList);
        return PageDTO.<TeamTaskDTO>builder()
                .currentPage(currentPage)
                .list(teamTaskDTOList)
                .totalPage((long) Math.ceil((count * 1.0) / pageDTO.getSize()))
                .size((long) teamTaskDOList.size())
                .build();
    }

    @Override
    public TeamDetailVO getDetail(@NonNull Long teamId) {
        TeamTaskDTO teamTaskDTO = this.getOne(teamId);
        int memberAmount = memberService.countMembersByTeamId(teamId);
        UserInfoDTO owner = userInfoService.getOneSafely(teamTaskDTO.getUserId());
        NextSignVO nextSign;
        try {
            nextSign = this.getNextSign(teamTaskDTO);
        } catch (GlobalException e) {
            nextSign = new NextSignVO();
        }

        TeamDetailVO teamDetailVO = new TeamDetailVO();
        BeanUtils.copyProperties(teamTaskDTO, teamDetailVO);
        teamDetailVO.setMemberAmount(memberAmount)
                    .setOwner(owner)
                    .setNextStartTime(nextSign.getNextStartTime())
                    .setNextEndTime(nextSign.getNextEndTime())
                    .setIsMember(memberService.isMember(teamId, UserContext.get().getUserId()))
                    .setIsOwner(UserContext.get().getUserId().equals(owner.getUserId()) ? 1 : 0);
        // 获取子任务
        List<TeamSubTaskDTO> teamSubTaskDTOList = teamSubTaskService.listByTeamId(teamId);
        teamDetailVO.setSubTaskList(teamSubTaskDTOList);
        // 点赞
        int isLike = likeService.isLike(LikeKey.TEAM, teamId, UserContext.get().getUserId());
        int likeCount = likeService.getTeamLikeCount(teamId);
        teamDetailVO.setIsLike(isLike)
                .setLikeCount(likeCount);
        asyncTaskService.reportAction(UserContext.get().getUserId(), TEAM_DETAIL, teamId, TEAM_TASK);
        return teamDetailVO;
    }

    @Override
    public NextSignVO getNextSign(Long teamId) {
        // 验证团队是否存在
        TeamTaskDTO teamTaskDTO = this.getOne(teamId);
        return this.getNextSign(teamTaskDTO);
    }

    private NextSignVO getNextSign(TeamTaskDTO teamTaskDTO) {
        // 要求任务进行中
        if (!TaskStatus.DOING.equals(teamTaskDTO.getTeamStatus())) {
            throw new GlobalException(CodeMsgEnum.TEAM_IS_END);
        }
        LocalDateTime nowTime = LocalDateTimeUtil.now(TimeZoneContext.get());
        List<TeamRecordDO> teamRecordDOList = teamRecordMapper.selectList(
                new QueryWrapper<TeamRecordDO>().eq("team_id", teamTaskDTO.getTeamId())
                                                .gt("next_end_time", nowTime)
                                                .orderByAsc("team_record_id")
        );

        // 装配必要的团队信息
        NextSignVO nextSignVO = new NextSignVO();
        BeanUtils.copyProperties(teamTaskDTO, nextSignVO);
        List<TeamSubTaskDTO> teamSubTaskDTOList = teamSubTaskService.listByTeamId(teamTaskDTO.getTeamId());
        nextSignVO.setSubTaskList(teamSubTaskDTOList);

        // 如果没有下一次的签到信息，就直接生成后返回
        if (CollectionUtils.isEmpty(teamRecordDOList)) {
            BeanUtils.copyProperties(this.addTeamRecord(teamTaskDTO, 1), nextSignVO);
            return nextSignVO;
        }


        // 倒序取距离当前时间最近一次的签到信息，签到开始时间早于当前时间的
        TeamRecordDO teamRecordSrc = null;
        int i;
        for (i = teamRecordDOList.size() - 1; i >= 0; i--) {
            TeamRecordDO teamRecordDO = teamRecordDOList.get(i);
            if (teamRecordDO.getNextStartTime().isBefore(nowTime)) {
                teamRecordSrc = teamRecordDO;
                break;
            }
        }
        // 如果没有就取最近一次签到信息，签到开始时间晚于当前时间的
        if (teamRecordSrc == null) {
            teamRecordSrc = teamRecordDOList.get(0);
            BeanUtils.copyProperties(teamRecordSrc, nextSignVO);
            return nextSignVO;
        }

        // 判断是否已经签到了，result == 1说明学员已经签到了最近一次
        int result = memberService.hasSignedIn(teamRecordSrc.getTeamRecordId(), UserContext.get().getUserId());
        if (result == 1) {
            // 已经签到了则需要取下一次签到信息，直接取 / 新增 取决于当前数据库是否已经生成
            teamRecordSrc = i < teamRecordDOList.size() - 1 ?
                    teamRecordDOList.get(i + 1) : this.addTeamRecord(teamTaskDTO, 2);
        }

        // 这种做法会导致在某个时段内一直只有两条。比如firstStartTime:11-21; firstEndTime:11-31; freq:1;
        // 用户在11-25日加入时，可以连续签到 11-21~11-31 和 11-22~12-1，但是之后会一直拿不到签到信息，需要等到过了11-31后才能拿到；
        // 因为此时 teamRecordDOList 的 size 才小于 2  ↓↓↓↓↓
        // 如果当前已经生成了下下次签到的记录就直接返回，否则生成下下次签到记录
        // 假设还没有签到最近一次的
//        TeamRecordDO teamRecordSrc = teamRecordDOList.get(0);
//        int result = memberService.hasSignedIn(teamRecordSrc.getTeamRecordId(), UserContext.get().getUserId());
//        // 判断是否已经签到了，result == 1说明学员已经签到了最近一次
//        if (result == 1) {
//             teamRecordSrc = (teamRecordDOList.size() == 2) ?
//                    teamRecordDOList.get(1) : this.addTeamRecord(teamTaskDTO, 2);
//        }
        BeanUtils.copyProperties(teamRecordSrc, nextSignVO);
        return nextSignVO;
    }

    @Override
    public List<NextSignVO> getAllNextSigns(Long userId, List<Long> teamIdList) {
        if (CollectionUtils.isEmpty(teamIdList)) {
            teamIdList = memberService.getTeamIdsByUserId(userId);
        }
        List<NextSignVO> signVOList = Lists.newArrayList();
        teamIdList.forEach(teamId -> {
            try {
                NextSignVO nextSign = getNextSign(teamId);
                signVOList.add(nextSign);

            } catch (Exception e) {
                log.info("【获取签到信息列表】userId = [{}], teamId = [{}], e = [{}]",
                        userId, teamId, e);
            }
        });
        return signVOList;
    }

    @Override
    public NextSignVO joinTeam(Long teamId) {
        TeamTaskDTO teamTaskDTO = this.getOne(teamId);
        String localTimeZone = TimeZoneContext.get();
        LocalDateTime localNow = LocalDateTimeUtil.now(localTimeZone);
        if (localNow.toLocalDate().isAfter(teamTaskDTO.getStartDate())) {
            log.error("【加入团队】已超过加入团队的截止日期，startDate() = [{}]", teamTaskDTO.getStartDate());
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }

        NextSignVO nextSign = this.getNextSign(teamTaskDTO);
        // 创建者作为新成员写入成员表

        TeamMemberDTO memberDTO = new TeamMemberDTO()
                .setTeamId(teamId)
                .setTeamRole(TeamRole.MEMBER)
                .setLocalTimeZone(localTimeZone)
                .setLocalCreateTime(localNow)
                .setUserId(UserContext.get().getUserId());
        // 创建者默认发布第一条团队成员动态 此动态是没有所属的teamRecordId的，用teamId替代了
        TeamMemberRecordDTO memberRecordDTO = new TeamMemberRecordDTO()
                .setTeamId(teamId)
                .setTeamTitle(teamTaskDTO.getTeamTitle())
                .setTeamRecordId(teamId)
                .setUserActivity("欢迎"+ UserContext.get().getNickname() +"加入团队「" +  nextSign.getTeamTitle() +"」")
                .setActivityIcon(ActivityIcon.IC_JOIN)
                .setCreateSource(teamTaskDTO.getCreateSource())
                .setLocalTimeZone(localTimeZone)
                .setLocalCreateTime(localNow);
        memberService.addMember(memberDTO, memberRecordDTO);
        asyncTaskService.reportAction(teamTaskDTO.getUserId(), JOIN_TEAM, teamTaskDTO.getTeamId(), TEAM_TASK);
        return nextSign;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = SingleTaskException.class)
    public NextSignVO signIn(Long teamId, ActivityVO activityVO) {
        return this.signIn(teamId, activityVO, ActivityIcon.IC_SIGN);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, noRollbackFor = SingleTaskException.class)
    public NextSignVO giveUp(Long teamId) {
        ActivityVO activityVO = new ActivityVO();
        activityVO.setActivity("");
        return this.signIn(teamId, activityVO, ActivityIcon.IC_GIVE_UP);
    }


    private NextSignVO signIn(Long teamId, ActivityVO activityVO, int activityIcon) {
        TeamTaskDTO teamTaskDTO = this.getOne(teamId);
        // 判断当前用户是否团队成员
        if (memberService.isMember(teamId, UserContext.get().getUserId()) == 0) {
            log.error("【团队签到】用户不是团队成员");
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }

        NextSignVO nextSign = this.getNextSign(teamTaskDTO);
        LocalDateTime nowTime = LocalDateTimeUtil.now(TimeZoneContext.get());

        if (nowTime.isBefore(nextSign.getNextStartTime())) {
            // 未到签到时间
            throw new GlobalException(CodeMsgEnum.TEAM_NOT_SIGN_TIME);

        } else if (nowTime.isAfter(nextSign.getNextEndTime())) {
            // 超过签到时间
            log.error("【团队签到】成员逾期操作，nowTime = [{}], nextSign = [{}]", nowTime, nextSign);
            throw new GlobalException(CodeMsgEnum.TEAM_NOT_SIGN_TIME);

        }
        // 可以签到
        TeamMemberRecordDTO memberRecordDTO = new TeamMemberRecordDTO()
                .setTeamId(teamId)
                .setTeamTitle(teamTaskDTO.getTeamTitle())
                .setTeamRecordId(nextSign.getTeamRecordId())
                .setUserActivity(activityVO.getActivity())
                .setActivityImages(activityVO.getActivityImages())
                .setActivityIcon(activityIcon)
                .setCreateSource(teamTaskDTO.getCreateSource())
                .setLocalTimeZone(TimeZoneContext.get())
                .setLocalCreateTime(nowTime);
        memberService.addMemberRecord(memberRecordDTO);
        // 单次任务在这次签到完成后就直接完成了，没有下一次
        if (teamTaskDTO.getTeamFreq() == 0) {
            throw new SingleTaskException(CodeMsgEnum.TEAM_IS_END);
        }
        asyncTaskService.reportAction(UserContext.get().getUserId(), SIGN_IN, memberRecordDTO.getTeamRecordId(), TEAM_MEMBER_RECORD);

        return this.getNextSign(teamTaskDTO);
    }

   @Override
    public void endTeam(Long teamId) {
        Long currUserId = UserContext.get().getUserId();
        TeamTaskDO teamTaskDO = teamTaskMapper.selectOne(
                new QueryWrapper<TeamTaskDO>()
                        .eq("team_id", teamId)
                        .eq("user_id", currUserId)
        );
        if (teamTaskDO == null) {
            throw new GlobalException(CodeMsgEnum.TEAM_INVALID_BEHAVIOR);
        }

        teamTaskDO.setTeamStatus(TaskStatus.COMPLETE);
        teamTaskDO.setCompleteTime(LocalDateTimeUtil.now(teamTaskDO.getLocalTimeZone()));
        teamTaskMapper.updateById(teamTaskDO);
    }

    @Override
    public List<TeamTaskDTO> getAllActiveTeams() {
        List<TeamTaskDO> teamTaskDOList = teamTaskMapper.selectList(
                new QueryWrapper<TeamTaskDO>()
                        .ne("team_rank", 0)
        );

        return teamTaskDOList.stream()
                .map(teamTaskDO -> TeamTaskDTO.from(teamTaskDO, TeamTaskDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public int incrTeamRank(Long teamId, int e) {
        return teamTaskMapper.incrTeamRank(teamId, e);
    }

    @Override
    public TeamTaskDTO update(TeamTaskDTO teamTaskDTO) {
        TeamTaskDTO teamTaskDTOFromDB = getOne(teamTaskDTO.getTeamId());
        if (!UserContext.get().getUserId().equals(teamTaskDTOFromDB.getUserId())) {
            log.error("【修改团队信息】越权操作，user = [{}], team = [{}], update = [{}]",
                    UserContext.get(), teamTaskDTOFromDB, teamTaskDTO);
            throw new GlobalException(CodeMsgEnum.TEAM_INVALID_BEHAVIOR);
        }
        teamTaskMapper.updateById(teamTaskDTO.toDO(TeamTaskDO.class));
        return teamTaskDTO;
    }

    /**
     * 由团队添加下n次团队签到记录
     * n可能为1，即下一次的团队签到记录
     * n可能为2，即下下一次的团队签到记录
     * @param teamTaskDTO 团队信息
     */
    private TeamRecordDO addTeamRecord(TeamTaskDTO teamTaskDTO, int n) {
        LocalDateTime firstStartTime = teamTaskDTO.getFirstStartTime();
        LocalDateTime nowTime = LocalDateTimeUtil.now(TimeZoneContext.get());

        // 计算下n次签到的日期时间
        LocalDate firstStartDate = firstStartTime.toLocalDate();
        LocalDate nowDate = nowTime.toLocalDate();
        long period = nowDate.toEpochDay() - firstStartDate.toEpochDay();
        int teamFreq = teamTaskDTO.getTeamFreq();

        boolean isEnd = (teamFreq == 0 && period > 0) || teamTaskDTO.getCompleteTime() != null;
        if (isEnd) {
            // 如果是不重复的团队任务，并且已经过了签到日期或者有了完成时间，则提示已经结束
            throw new GlobalException(CodeMsgEnum.TEAM_IS_END);
        }
        // 处理不重复的任务
        if (teamFreq == 0 && n != 1) {
            throw new GlobalException(CodeMsgEnum.TEAM_IS_END);
        }
        long nextSignPeriod = (teamFreq == 0) ?
                0 : (long) Math.ceil((period * 1.0) / teamFreq) * teamFreq + teamFreq * (n - 1);
        LocalDateTime nextStartTime = firstStartTime.plusDays(nextSignPeriod);
        LocalDateTime nextEndTime = teamTaskDTO.getFirstEndTime().plusDays(nextSignPeriod);
        if (period < 0) {
            // 应对开始日期在当前日期之后
            nextStartTime = teamTaskDTO.getFirstStartTime();
            nextEndTime = teamTaskDTO.getFirstEndTime();
        }
        // 如果新生成的下一次签到结束时间 小于 当前时间（也就是说新生成的团队动态就已经是结束的），则需要再加一个 频率值
        if (nextEndTime.isBefore(nowTime)) {
            nextStartTime = nextStartTime.plusDays(teamFreq);
            nextEndTime = nextEndTime.plusDays(teamFreq);
        }

        // 插入下n次的团队签到记录
        TeamRecordDO nextTeamRecordDO = new TeamRecordDO().setNextStartTime(nextStartTime)
                                                      .setNextEndTime(nextEndTime)
                                                      .setTeamId(teamTaskDTO.getTeamId());
        teamRecordMapper.insert(nextTeamRecordDO);

        return nextTeamRecordDO;
    }

    private void assembleTeamList(List<TeamTaskDTO> teamTaskDTOList) {
        teamTaskDTOList.forEach(teamTaskDTO -> {
            Long teamId = teamTaskDTO.getTeamId();
            int isLike = likeService.isLike(LikeKey.TEAM, teamId, UserContext.get().getUserId());
            int likeCount = likeService.getTeamLikeCount(teamId);
            teamTaskDTO.setIsLike(isLike)
                    .setLikeCount(likeCount);
        });
        asyncTaskService.reportAction(UserContext.get().getUserId(), BROWSE_TEAM, -1L, TEAM_TASK);
    }
}
