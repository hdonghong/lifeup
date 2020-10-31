package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.model.dto.ReportRecordDTO;
import com.hdh.lifeup.model.vo.ReportResultVO;
import com.hdh.lifeup.service.ReportRecordService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ReportRecordController class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Api(description = "举报记录模块")
@RestController
@RequestMapping("/report/records")
public class ReportRecordController {

    private ReportRecordService reportRecordService;

    @Autowired
    public ReportRecordController(ReportRecordService reportRecordService) {
        this.reportRecordService = reportRecordService;
    }

    @ApiLimiting
    @ApiOperation(value = "举报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/new")
    public ResultVO<ReportResultVO> addRecord(@RequestBody @Valid ReportRecordDTO reportRecordDTO) {
        return Result.success(
            reportRecordService.insert(reportRecordDTO)
        );
    }
}
