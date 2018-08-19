package com.hdh.lifeup.controller;

import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserInfoService userInfoService;

    /**
     * paramType：参数放在哪个地方
                 header-->请求参数的获取：@RequestHeader
                 query-->请求参数的获取：@RequestParam
                 path（用于restful接口）-->请求参数的获取：@PathVariable
                 body（不常用）
                 form（不常用）
        详细参考：http://www.cnblogs.com/java-zhao/p/5348113.html
     */
    @ApiOperation(value = "查询指定用户的个人信息", notes = "用于打开自己的个人信息页面，以及其他用户的个人信息页面。")
    @ApiImplicitParam(name = "userId", value = "用户主键", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{userId}")
    public ResultVO<UserInfoDTO> getOne(@PathVariable("userId") Long userId) {
        UserInfoDTO getResult = userInfoService.getOne(userId);
        return Result.success(getResult);
    }

    @ApiOperation(value = " 修改自己的公众信息", notes = "昵称、性别、居住地。还要讨论下要不要做头像？")
    @PutMapping("/profile/{userId}")
    public ResultVO<UserInfoDTO> updateProfile(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }

    @ApiOperation(value = " 修改自己的私人信息", notes = "比如密码修改")
    @PutMapping("/account/{userId}")
    public ResultVO<UserInfoDTO> updateAccount(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }
}
