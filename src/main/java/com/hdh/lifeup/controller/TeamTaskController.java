package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.TeamTaskDTO;
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
        teamTaskVO.setTeamTitle(SensitiveFilter.filter(teamTaskVO.getTeamTitle()))
                .setTeamDesc(SensitiveFilter.filter(teamTaskVO.getTeamDesc()));
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
    @ApiOperation(value = "获取用户所有下一次要签到的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "teamId", required = true, paramType = "path", dataType = "long"),
    })
    @GetMapping("/next_signs")
    public ResultVO<List<NextSignVO>> getAllNextSigns() {
        return Result.success(
                teamTaskService.getAllNextSigns(UserContext.get().getUserId())
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取团队列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping
    public ResultVO<PageDTO<TeamTaskDTO>> getPage(PageDTO pageDTO, String teamTitle) {
        return Result.success(
                teamTaskService.page(pageDTO, teamTitle)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "加入团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/{teamId}")
    public ResultVO<?> joinTeam(@PathVariable Long teamId) {
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
        activityVO.setActivity(SensitiveFilter.filter(activityVO.getActivity()));

        return Result.success(
                teamTaskService.signIn(teamId, activityVO)
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
        teamEditVO.setTeamTitle(SensitiveFilter.filter(teamEditVO.getTeamTitle()))
                .setTeamDesc(SensitiveFilter.filter(teamEditVO.getTeamDesc()));

        TeamTaskDTO teamTaskDTO = new TeamTaskDTO();
        BeanUtils.copyProperties(teamEditVO, teamTaskDTO);
        teamTaskService.update(teamTaskDTO);
        return Result.success();
    }


}
