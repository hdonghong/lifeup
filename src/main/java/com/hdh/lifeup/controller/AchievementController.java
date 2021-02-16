package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.vo.UserAchievementVO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserAchievementDTO;
import com.hdh.lifeup.service.UserAchievementService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.model.vo.UserListVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AchievementController class<br/>
 *
 * @author hdonghong
 * @since 2018/10/28
 */
@RestController
@RequestMapping("/achieve")
public class AchievementController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserAchievementService userAchievementService;

    @ApiLimiting
    @ApiOperation(value = "获取关注者的周属性排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/rank/following")
    public ResultVO<PageDTO<UserListVO>> followingsRank(PageDTO pageDTO) {
        return Result.success(
                userInfoService.getFollowingsRank(UserContext.get().getUserId(), pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "同步成就记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/sync")
    public ResultVO<Void> syncAchievement(@RequestBody List<UserAchievementVO> userAchievementAOList) {
        Long userId = UserContext.get().getUserId();
        userAchievementAOList.forEach(userAchievementAO -> {
            userAchievementAO.setUserId(userId);
            // userAchievementService.sync(userAchievementAO);
        });
        return Result.success();
    }


    @ApiLimiting
    @ApiOperation(value = "获取用户的成就记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping
    public ResultVO<List<UserAchievementVO>> listAchievements(
            @RequestParam(value = "hasComplete", defaultValue = "1", required = false) Integer hasComplete) {
        List<UserAchievementDTO> userAchievementDTOList = userAchievementService.listAchievements(
                UserContext.get().getUserId(), hasComplete);
        List<UserAchievementVO> achievementAOList = userAchievementDTOList.stream().map(userAchievementDTO -> {
            UserAchievementVO userAchievementAO = new UserAchievementVO();
            BeanUtils.copyProperties(userAchievementDTO, userAchievementAO);
            return userAchievementAO;
        }).collect(Collectors.toList());
        return Result.success(achievementAOList);
    }

    @ApiLimiting
    @ApiOperation(value = "删除用户的成就记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @DeleteMapping
    public ResultVO<?> deleteUserAchievement() {
        Long userId = UserContext.get().getUserId();
        userAchievementService.deleteByUserId(userId);
        return Result.success();
    }
}
