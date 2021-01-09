package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.UserInfoDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
