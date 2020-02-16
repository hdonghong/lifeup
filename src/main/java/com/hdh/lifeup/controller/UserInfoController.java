package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.config.QiniuConfig;
import com.hdh.lifeup.model.constant.TaskConst;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.RecordDTO;
import com.hdh.lifeup.model.dto.TeamTaskDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.service.TeamTaskService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.util.UploadUtil;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.model.vo.UserDetailVO;
import com.hdh.lifeup.model.vo.UserListVO;
import com.hdh.lifeup.util.sensitive.SensitiveFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserInfoController class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Api(description = "用户模块")
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private UserInfoService userInfoService;
    private TeamMemberService teamMemberService;
    private TeamTaskService teamTaskService;
    private QiniuConfig qiniuConfig;

    @Autowired
    public UserInfoController(UserInfoService userInfoService,
                              TeamMemberService teamMemberService,
                              TeamTaskService teamTaskService,
                              QiniuConfig qiniuConfig) {
        this.userInfoService = userInfoService;
        this.teamMemberService = teamMemberService;
        this.teamTaskService = teamTaskService;
        this.qiniuConfig = qiniuConfig;
    }

    /**
     * paramType：参数放在哪个地方
                 header-->请求参数的获取：@RequestHeader
                 query-->请求参数的获取：@RequestParam
                 path（用于restful接口）-->请求参数的获取：@PathVariable
                 body（不常用）
                 form（不常用）
        详细参考：http://www.cnblogs.com/java-zhao/p/5348113.html
     */
    @ApiLimiting
    @ApiOperation(value = "查询自己的个人信息", notes = "用于打开自己的个人信息页面")
    @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String")
    @GetMapping("/profile")
    public ResultVO<UserInfoDTO> getMine() {
        return Result.success(UserContext.get());
    }

    @ApiLimiting
    @ApiOperation(value = "查询用户的个人详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", paramType = "path", dataType = "Long"),
    })
    @GetMapping(value = {"/detail", "/{userId}/detail"})
    public ResultVO<UserDetailVO> getDetail(@PathVariable(value = "userId", required = false) Long userId) {
        return Result.success(
                userInfoService.getDetailById(userId)
        );
    }

    @ApiLimiting
    @ApiOperation(value = " 修改自己的公众信息", notes = "不传id，昵称、性别、居住地。还要讨论下要不要做头像？")
    @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String")
    @PutMapping("/profile")
    public ResultVO<UserInfoDTO> updateProfile(@RequestBody UserInfoDTO userInfoDTO) {
        userInfoDTO.setNickname(SensitiveFilter.filter(userInfoDTO.getNickname()));

        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }

    @ApiLimiting
    @ApiOperation(value = " 上传头像")
    @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String")
    @PostMapping("/avatar")
    public ResultVO<String> upload(MultipartFile avatarImage) {
        String imageUrl = UploadUtil.uploadImage(avatarImage, QiniuConfig.AVATAR_URI, qiniuConfig);
        UserInfoDTO userInfoDTO = UserContext.get().setUserHead(imageUrl);
        userInfoService.update(userInfoDTO);
        return Result.success(imageUrl);
    }

    @ApiLimiting
    @ApiOperation(value = " 修改自己的私人信息", notes = "不传id，比如密码修改。目前修改公众信息和私人信息接口一样，但以后不会一样，客户端需要按功能含义使用接口，不传递不必要的数据")
    @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String")
    @PutMapping("/account")
    public ResultVO<UserInfoDTO> updateAccount(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }

    @ApiLimiting
    @ApiOperation(value = "查询指定用户的动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", paramType = "path", dataType = "Long"),
    })
    @GetMapping(value = {"/activities", "/{userId}/activities"})
    public ResultVO<PageDTO<RecordDTO>> getActivities(
            @PathVariable(value = "userId", required = false) Long userId, PageDTO pageDTO) {
        return Result.success(
                teamMemberService.pageUserRecords(userId, pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "用户删除动态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "memberRecordId", paramType = "path", dataType = "Long"),
    })
    @DeleteMapping(value = {"/activities/{memberRecordId}"})
    public ResultVO delActivity(@PathVariable Long memberRecordId) {
        teamMemberService.delUserRecord(memberRecordId, UserContext.get().getUserId());
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "获取指定用户加入的团队列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", paramType = "path", dataType = "Long", example = "0进行中；2已完成；"),
            @ApiImplicitParam(name = "teamStatus", dataType = "int"),
    })
    @GetMapping(value = {"/teams", "/{userId}/teams"})
    public ResultVO<PageDTO<TeamTaskDTO>> getTeams(
            @PathVariable(value = "userId", required = false) Long userId, PageDTO pageDTO, Integer teamStatus,
            @RequestParam(defaultValue = "false", required = false) Boolean isOwner) {
        if (teamStatus == null) {
            teamStatus = TaskConst.TaskStatus.DOING;
        }
        return Result.success(
                teamTaskService.pageUserTeams(userId, pageDTO, teamStatus, isOwner)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "关注，我想要关注的用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", required = true, paramType = "path", dataType = "Long"),
    })
    @PostMapping("/following/{userId}")
    public ResultVO<?> follow(@PathVariable(value = "userId") Long userId) {
        userInfoService.follow(userId);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "取消关注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", required = true, paramType = "path", dataType = "Long"),
    })
    @DeleteMapping("/following/{userId}")
    public ResultVO<?> deleteFollowing(@PathVariable(value = "userId") Long userId) {
        userInfoService.deleteFollowing(userId);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "获取{user}的关注", notes = "userId可传可不传，不传递表示当前用户，即'我的关注'")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", paramType = "path", dataType = "Long"),
    })
    @GetMapping(value = {"/following", "/{userId}/following"})
    public ResultVO<PageDTO<UserListVO>> getFollowings(
            @PathVariable(value = "userId", required = false) Long userId, PageDTO pageDTO) {
        return Result.success(
            userInfoService.getFollowings(userId, pageDTO)
        );
    }

    @ApiLimiting
    @ApiOperation(value = "获取{user}的跟随者", notes = "userId可传可不传，不传递表示当前用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "userId", paramType = "path", dataType = "Long"),
    })
    @GetMapping(value = {"/follower", "/{userId}/follower"})
    public ResultVO<PageDTO<UserListVO>> getFollowers(
            @PathVariable(value = "userId", required = false) Long userId, PageDTO pageDTO) {
        return Result.success(
                userInfoService.getFollowers(userId, pageDTO)
        );
    }

    /**
     *
     * @param pageDTO
     * @param scope
     * @param filter 过滤一些圈子，
     * @return
     */
    @ApiLimiting
    @ApiOperation(value = "获取我的朋友圈scope=2 或 所有人的scope=3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping(value = {"/moments", "/moments/{scope}"})
    public ResultVO<PageDTO<RecordDTO>> getMoments(PageDTO pageDTO,
                                                   @PathVariable(value = "scope", required = false) Integer scope,
                                                   @RequestParam(defaultValue = "0", required = false) Integer filter,
                                                   @RequestParam(defaultValue = "1", required = false) Integer createSource) {
        // 限定只能是朋友圈 或者 所有人
        if (!TaskConst.ActivityScope.ALL.equals(scope)) {
            scope = TaskConst.ActivityScope.MYFOLLOWERS;
        }
        return Result.success(
                teamMemberService.getMoments(pageDTO, scope, filter, createSource)
        );
    }
}
