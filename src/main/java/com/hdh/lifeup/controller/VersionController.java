package com.hdh.lifeup.controller;

import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import com.hdh.lifeup.vo.VersionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VersionController class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Api(description = "app版本控制模块")
@RestController
@RequestMapping("/version")
public class VersionController {

    private VersionVO versionVO;

    @Autowired
    public VersionController(VersionVO versionVO) {
        this.versionVO = versionVO;
    }

    @ApiOperation(value = "获取最新版本号")
    @GetMapping
    public ResultVO<VersionVO> getNewVersion() {
        return Result.success(versionVO);
    }
}
