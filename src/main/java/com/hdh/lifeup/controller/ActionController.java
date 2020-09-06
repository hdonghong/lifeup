package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.ActionRecordDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserActionDTO;
import com.hdh.lifeup.model.dto.UserActionRecordDTO;
import com.hdh.lifeup.model.vo.ActionRecordVO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.model.vo.UserListVO;
import com.hdh.lifeup.service.ActionRecordService;
import com.hdh.lifeup.service.UserActionService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ActionController class<br/>
 *
 * @author hdonghong
 * @since 2020/07/10
 */
@Api(description = "用户行为模块")
@RestController
@RequestMapping("/action")
public class ActionController {

    @Resource
    private ActionRecordService actionRecordService;
    @Resource
    private UserActionService userActionService;

    @ApiLimiting
    @ApiOperation(value = "上报用户动态")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/report")
    public ResultVO<Void> reportAction(@RequestBody ActionRecordVO actionRecordVO) {
        ActionRecordDTO actionRecordDTO = new ActionRecordDTO()
            .setActionId(actionRecordVO.getActionId())
            .setActionSource(actionRecordVO.getActionSource())
            .setUserId(UserContext.get().getUserId());
        actionRecordService.reportAction(actionRecordDTO);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "获取排行榜详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "lifeup-language", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/list")
    public ResultVO<List<UserActionDTO>> listAction(@RequestHeader(value="lifeup-language") String language) {
        return Result.success(
                userActionService.listAll(language)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "查询指定用户的动态")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "lifeup-language", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping(value = {"/record"})
    public ResultVO<PageDTO<UserActionRecordDTO>> pageActionRecord(
        @RequestHeader(value="lifeup-language") String language, PageDTO pageDTO) {
        return Result.success(
            actionRecordService.pageByUser(language, UserContext.get().getUserId(),
                pageDTO.getCurrentPage().intValue(), pageDTO.getSize().intValue())
        );
    }

    @ApiLimiting
    @ApiOperation(value = "查询指定用户的动态分组")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
        @ApiImplicitParam(name = "lifeup-language", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping(value = {"/record/group"})
    public ResultVO<List<UserActionRecordDTO>> listActionRecordGroup(
        @RequestHeader(value="lifeup-language") String language) {
        return Result.success(
            actionRecordService.listGroupByAction(language, UserContext.get().getUserId()));
    }
}
