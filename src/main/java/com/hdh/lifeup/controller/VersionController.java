package com.hdh.lifeup.controller;

import com.hdh.lifeup.model.dto.AppVersionDTO;
import com.hdh.lifeup.service.AppVersionService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.ResultVO;
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

    private AppVersionService appVersionService;

    @Autowired
    public VersionController(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @ApiOperation(value = "获取最新版本号")
    @GetMapping
    public ResultVO<AppVersionDTO> getLastVersion() {
        return Result.success(
                appVersionService.getLastVersion()
        );
    }
}
