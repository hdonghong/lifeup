package com.hdh.lifeup.controller;

import com.google.common.collect.Maps;
import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

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
    @ApiLimiting
    @ApiOperation(value = "查询指定用户的个人信息", notes = "用于打开自己的个人信息页面，以及其他用户的个人信息页面。")
    @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String")
    @GetMapping("/profile")
    public ResultVO<UserInfoDTO> getMine() {
        return Result.success(UserContext.get());
    }

    @ApiLimiting
    @ApiOperation(value = " 修改自己的公众信息", notes = "不传id，昵称、性别、居住地。还要讨论下要不要做头像？")
    @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String")
    @PutMapping("/profile")
    public ResultVO<UserInfoDTO> updateProfile(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }

    @ApiLimiting
    @ApiOperation(value = " 修改自己的私人信息", notes = "不传id，比如密码修改。目前修改公众信息和私人信息接口一样，但以后不会一样，客户端需要按功能含义使用接口，不传递不必要的数据")
    @ApiImplicitParam(name = "AUTHENTICITY_TOKEN", required = true, paramType = "header", dataType = "String")
    @PutMapping("/account")
    public ResultVO<UserInfoDTO> updateAccount(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfoDTO updateResult = userInfoService.update(userInfoDTO);
        return Result.success(updateResult);
    }
}
