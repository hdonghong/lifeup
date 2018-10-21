package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dto.ReportTypeDTO;
import com.hdh.lifeup.dto.TaskDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.service.ReportTypeService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ReportTypeController class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Api(description = "举报类型模块")
@RestController
@RequestMapping("/report/types")
public class ReportTypeController {

    private ReportTypeService reportTypeService;

    @Autowired
    public ReportTypeController(ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @ApiLimiting
    @ApiOperation(value = "获取所有举报类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping
    public ResultVO<List<ReportTypeDTO>> listAll(@PathVariable Long taskId) {
        return Result.success(
                reportTypeService.listAll()
        );
    }
}
