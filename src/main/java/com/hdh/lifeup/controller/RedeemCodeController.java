package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.service.RedeemCodeService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * RedeemCodeController class<br/>
 *
 * @author hdonghong
 * @since 2020/04/06
 */
@Api(description = "兑换码模块")
@RestController
@RequestMapping(value = "/codes", headers = {"Referer=lifeup.D2D0706D81E7DB115451B33841A8BF09"})
public class RedeemCodeController {

    @Resource
    private RedeemCodeService redeemCodeService;

    @ApiOperation(value = "兑换", notes = "指定动态id")
    @ApiLimiting(maxAccess = 10)
    @PostMapping("/redeem")
    public ResultVO<?> redeemCode(String redeemCode) {
        Long userId = UserContext.get().getUserId();
        boolean result = redeemCodeService.redeem(userId, redeemCode);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("redeemStatus", result ? 1 : 0);
        return Result.success(map);
    }
}
