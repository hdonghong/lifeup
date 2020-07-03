package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.vo.LikeVO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.service.LikeService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * LikeController class<br/>
 *
 * @author hdonghong
 * @since 2019/05/25
 */
@Api(description = "点赞模块")
@RestController
@RequestMapping(value = "/likes", headers = {"Referer=lifeup.D2D0706D81E7DB115451B33841A8BF09"})
public class LikeController {

    @Autowired
    private LikeService likeService;

    @ApiOperation(value = "点赞动态", notes = "指定动态id")
    @ApiLimiting(maxAccess = 10)
    @PostMapping("/activities/{memberRecordId}")
    public ResultVO<LikeVO> likeActivity(@PathVariable Long memberRecordId) {
        Long userId = UserContext.get().getUserId();
        int count = likeService.doLike(userId, memberRecordId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }


    @ApiOperation(value = "取消点赞动态", notes = "指定动态id")
    @ApiLimiting(maxAccess = 10)
    @DeleteMapping("/activities/{memberRecordId}")
    public ResultVO<LikeVO> undoLikeActivity(@PathVariable Long memberRecordId) {
        Long userId = UserContext.get().getUserId();
        int count = likeService.undoLike(userId, memberRecordId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }

    @ApiOperation(value = "查看当前用户点赞数", notes = "")
    @ApiLimiting(maxAccess = 10)
    @GetMapping("/users/count")
    public ResultVO<LikeVO> userLikeCount() {
        int likeCount = likeService.getUserLikeCount(UserContext.get().getUserId());
        int exchangedLikeCount = likeService.getUserExchangedLikeCount(UserContext.get().getUserId());
        LikeVO likeVO = new LikeVO()
                .setLikeCount(likeCount)
                .setExchangedLikeCount(exchangedLikeCount);
        return Result.success(likeVO);
    }

    @ApiOperation(value = "点赞数兑换", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "兑换的赞数", required = true, paramType = "json"),
    })
    @ApiLimiting(maxAccess = 10)
    @PostMapping("/exchange")
    public ResultVO<LikeVO> exchange(@RequestBody Map<String, Integer> params) {
        Integer count = params.get("count");
        if (count == null || count < 1) {
            throw new GlobalException(CodeMsgEnum.PARAMETER_ERROR);
        }
        Long userId = UserContext.get().getUserId();
        // 本次实际兑换的个数
        int currExchangedLikeCount = likeService.exchangeLike(userId, count);
        LikeVO likeVO = new LikeVO()
                .setExchangedLikeCount(currExchangedLikeCount);
        return Result.success(likeVO);
    }

    @ApiOperation(value = "点赞团队", notes = "指定teamId")
    @ApiLimiting(maxAccess = 10)
    @PostMapping("/activities/{teamId}")
    public ResultVO<LikeVO> likeTeam(@PathVariable Long teamId) {
        Long userId = UserContext.get().getUserId();
        int count = likeService.doLikeTeam(userId, teamId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }


    @ApiOperation(value = "取消点赞团队", notes = "指定teamId")
    @ApiLimiting(maxAccess = 10)
    @DeleteMapping("/activities/{teamId}")
    public ResultVO<LikeVO> undoLikeTeam(@PathVariable Long teamId) {
        Long userId = UserContext.get().getUserId();
        int count = likeService.undoLikeTeam(userId, teamId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }
}
