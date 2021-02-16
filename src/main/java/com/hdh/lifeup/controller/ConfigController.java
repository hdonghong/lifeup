package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.query.BatchQuery;
import com.hdh.lifeup.model.query.RuleCfgQuery;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.model.vo.RuleCfgVO;
import com.hdh.lifeup.service.RuleCfgService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ConfigController class<br/>
 *
 * @author hdonghong
 * @since 2020/10/08
 */
@Api(description = "配置")
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private RuleCfgService ruleCfgService;

    @ApiLimiting(toAuth = false)
    @ApiOperation(value = "获取规则匹配的配置，可能存在多个配置匹配到同一规则")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/rule")
    public ResultVO<List<RuleCfgVO>> matchRuleConfig(RuleCfgQuery ruleCfgQuery) {
        UserInfoDTO currentUser = UserContext.get();
        Long userId = (currentUser == null) ? null : currentUser.getUserId();
        return Result.success(
            ruleCfgService.match(userId, ruleCfgQuery)
        );
    }

    @ApiLimiting(toAuth = false)
    @ApiOperation(value = "批量获取规则匹配的配置，可能存在多个配置匹配到同一规则")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/rule/batch")
    public ResultVO<List<BatchQuery>> matchRuleConfigs(@RequestBody List<BatchQuery<RuleCfgQuery>> ruleCfgQueryList) {
        UserInfoDTO currentUser = UserContext.get();
        Long userId = (currentUser == null) ? null : currentUser.getUserId();
        List<BatchQuery> result = ruleCfgQueryList.stream().map(ruleCfgBatchQuery -> {
            List<RuleCfgVO> ruleCfgList = ruleCfgService.match(userId, ruleCfgBatchQuery.getT());
            return new BatchQuery()
                .setUuid(ruleCfgBatchQuery.getUuid())
                .setT(ruleCfgList);
        }).collect(Collectors.toList());
        return Result.success(result);
    }
}
