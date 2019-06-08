package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.AttributeDTO;
import com.hdh.lifeup.service.AttributeService;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.model.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TaskController class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Api(description = "属性表模块")
@RestController
@RequestMapping("/user")
public class AttributeController {

    private AttributeService attributeService;

    @Autowired
    public AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    @ApiLimiting
    @ApiOperation(value = "查询指定人物属性", notes = "自己的人物，详细属性，包括经验")
    @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String")
    @GetMapping("/attribute")
    public ResultVO<AttributeDTO> getAttribute() {
        AttributeDTO attributeDTO = attributeService.getByUserId(UserContext.get().getUserId());
        return Result.success(attributeDTO);
    }

    @ApiLimiting
    @ApiOperation(value = "修改指定人物属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "AttributeDTO", required = true, paramType = "post", dataType = "json"),
    })
    @PostMapping("/attribute")
    public ResultVO<AttributeDTO> updateAttribute(@RequestBody AttributeDTO attributeDTO) {
        return Result.success(attributeService.update(attributeDTO));
    }
}
