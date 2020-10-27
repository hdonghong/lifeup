package com.hdh.lifeup.controller;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.TimeZoneContext;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.constant.CommonConst;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.*;
import com.hdh.lifeup.service.TeamTaskService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.util.sensitive.SensitiveFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hdh.lifeup.model.constant.CommonConst.TIME_ZONE_GMT8;

/**
 * TeamTaskController class<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
@Api(description = "团队任务模块")
@RestController
@RequestMapping("/teams")
public class TeamTaskController {

    private TeamTaskService teamTaskService;

    @Autowired
    public TeamTaskController(TeamTaskService teamTaskService) {
        this.teamTaskService = teamTaskService;
    }

    @ApiLimiting
    @ApiOperation(value = "新建团队任务")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/new")
    public ResultVO<NextSignVO> addTeam(@RequestBody TeamTaskVO teamTaskVO) {
        // 非海外的需要过滤
        if (!Objects.equal(CommonConst.CreateSource.OVERSEAS, teamTaskVO.getCreateSource())) {
            teamTaskVO.setTeamTitle(SensitiveFilter.filter(teamTaskVO.getTeamTitle()))
                    .setTeamDesc(SensitiveFilter.filter(teamTaskVO.getTeamDesc()));
        }
        // 规定金币值的范围[0, 99]
        if (teamTaskVO.getCoin() < 0 || teamTaskVO.getCoinVariable() < 0
                || teamTaskVO.getCoin() + teamTaskVO.getCoinVariable() > 99) {
            return Result.error(CodeMsgEnum.TEAM_INVALID_COIN);
        }
        return Result.success(
                teamTaskService.addTeam(teamTaskVO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "团队详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/{teamId}")
    public ResultVO<TeamDetailVO> get(@PathVariable("teamId") Long teamId) {
        return Result.success(
                teamTaskService.getDetail(teamId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取下一次要签到的信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "teamId", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("/{teamId}/next_sign")
    public ResultVO<NextSignVO> getNextSign(@PathVariable("teamId") Long teamId) {
        return Result.success(
                teamTaskService.getNextSign(teamId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取用户所有/指定的下一次要签到的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/next_signs")
    public ResultVO<List<NextSignVO>> getAllNextSigns(Long[] teamIdArr) {
        List<Long> teamIdList = Lists.newArrayList(teamIdArr);
        return Result.success(
                teamTaskService.getAllNextSigns(UserContext.get().getUserId(), teamIdList)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取团队列表分页；createSource取值:1国内；2海外")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "createSource", required = false, paramType = "", dataType = "int", example = "来源，1国内；2海外"),
    })
    @GetMapping
    public ResultVO<PageDTO<TeamTaskDTO>> getPage(
            PageDTO pageDTO, String teamTitle, Integer teamRank,
            @RequestParam(defaultValue = "true", required = false) Boolean startDateFilter,
            @RequestParam(defaultValue = "1", required = false) Integer createSource) {
        return Result.success(
                teamTaskService.page(pageDTO, teamTitle, teamRank, startDateFilter, createSource)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "加入团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/{teamId}")
    public ResultVO<NextSignVO> joinTeam(@PathVariable Long teamId) {
        return Result.success(
                teamTaskService.joinTeam(teamId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "签到，发动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "teamId", required = true, paramType = "path", dataType = "long"),
    })
    @PostMapping("/{teamId}/sign")
    public ResultVO<NextSignVO> sizgnIn(
            @PathVariable Long teamId, @RequestBody ActivityVO activityVO) {
        // 动态暂时不过滤敏感词
//        activityVO.setActivity(SensitiveFilter.filter(activityVO.getActivity()));

        return Result.success(
                teamTaskService.signIn(teamId, activityVO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "放弃当前")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "teamId", required = true, paramType = "path", dataType = "long"),
    })
    @PostMapping("/{teamId}/giveUp")
    public ResultVO<NextSignVO> giveUp(@PathVariable Long teamId) {
        return Result.success(
                teamTaskService.giveUp(teamId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "终止团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/{teamId}/end")
    public ResultVO<?> end(@PathVariable("teamId") Long teamId) {
        teamTaskService.endTeam(teamId);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "编辑团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PutMapping("/{teamId}")
    public ResultVO<?> editTeam(@RequestBody TeamEditVO teamEditVO) {
        if (Objects.equal(TimeZoneContext.get(), TIME_ZONE_GMT8)) {
            teamEditVO.setTeamTitle(SensitiveFilter.filter(teamEditVO.getTeamTitle()))
                    .setTeamDesc(SensitiveFilter.filter(teamEditVO.getTeamDesc()));
        }
        // 规定金币值的范围[0, 99]
        if (teamEditVO.getCoin() < 0 || teamEditVO.getCoinVariable() < 0
                || teamEditVO.getCoin() + teamEditVO.getCoinVariable() > 99) {
            return Result.error(CodeMsgEnum.TEAM_INVALID_COIN);
        }
        TeamTaskDTO teamTaskDTO = new TeamTaskDTO();
        BeanUtils.copyProperties(teamEditVO, teamTaskDTO);
        teamTaskService.update(teamTaskDTO);
        return Result.success();
    }


}
