package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.TeamTaskDTO;
import com.hdh.lifeup.service.TeamTaskService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
        @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/new")
    public ResultVO<NextSignVO> addTeam(@RequestBody TeamTaskVO teamTaskVO) {
        return Result.success(
                teamTaskService.addTeam(teamTaskVO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "团队详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
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
        @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
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
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
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
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping
    public ResultVO<PageDTO<TeamTaskDTO>> getPage(PageDTO pageDTO) {

        return Result.success(
                teamTaskService.page(pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "加入团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/{teamId}")
    public ResultVO<?> joinTeam(@PathVariable Long teamId) {
        return Result.success(
                teamTaskService.joinTeam(teamId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "签到")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "teamId", required = true, paramType = "path", dataType = "long"),
    })
    @PostMapping("/{teamId}/sign")
    public ResultVO<NextSignVO> signIn(@PathVariable Long teamId, @RequestBody ActivityVO activityVO) {
        return Result.success(
                teamTaskService.signIn(teamId, activityVO)
        );
    }


}
