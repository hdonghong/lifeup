package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ActionController class<br/>
 *
 * @author hdonghong
 * @since 2020/07/10
 */
@RestController
@RequestMapping("/action")
public class ActionController {

    @ApiLimiting
    @ApiOperation(value = "上报用户动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/report")
    public ResultVO<Void> syncAchievement(Long actionId) {

        return Result.success();
    }
}
