package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.FeedbackDTO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.service.FeedbackService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FeedbackController class<br/>
 *
 * @author hdonghong
 * @since 2019/10/17
 */
@Api(description = "用户反馈模块")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @ApiLimiting
    @ApiOperation(value = "新增用户反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/new")
    public ResultVO addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        feedbackDTO.setUserId(UserContext.get().getUserId());
        feedbackService.insert(feedbackDTO);
        return Result.success();
    }

}
