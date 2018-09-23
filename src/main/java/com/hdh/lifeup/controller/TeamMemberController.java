package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.RecordDTO;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.MembersVO;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TeamMemberController class<br/>
 *
 * @author hdonghong
 * @since 2018/09/16
 */
@Api(description = "团队成员模块")
@RestController
@RequestMapping("/teams/{teamId}")
public class TeamMemberController {

    private TeamMemberService memberService;

    @Autowired
    public TeamMemberController(TeamMemberService memberService) {
        this.memberService = memberService;
    }


    @ApiLimiting
    @ApiOperation(value = "获取团队成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/members")
    public ResultVO<PageDTO<MembersVO>> getMembers(@PathVariable Long teamId, PageDTO pageDTO) {
        return Result.success(
                memberService.pageMembers(teamId, pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取团队成员动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/records")
    public ResultVO<PageDTO<RecordDTO>> getMemberRecords(@PathVariable Long teamId, PageDTO pageDTO) {
        return Result.success(
                memberService.pageMemberRecords(teamId, pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "成员退出团队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @DeleteMapping("/members/quit")
    public ResultVO<?> quitTeam() {
        return Result.success();
    }
}
