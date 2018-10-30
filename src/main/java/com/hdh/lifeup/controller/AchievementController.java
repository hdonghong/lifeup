package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import com.hdh.lifeup.vo.UserListVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AchievementController class<br/>
 *
 * @author hdonghong
 * @since 2018/10/28
 */
@RestController
@RequestMapping("/achieve")
public class AchievementController {

    private UserInfoService userInfoService;

    @Autowired
    public AchievementController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

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
}
