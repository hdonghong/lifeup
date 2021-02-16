package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.query.PageQuery;
import com.hdh.lifeup.model.vo.GoodsInfoVO;
import com.hdh.lifeup.model.vo.GoodsShareVO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.service.MarketGoodsService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * MarketGoodsController class<br/>
 *
 * @author hdonghong
 * @since 2020/10/18
 */
@Api(description = "商品")
@RestController
@RequestMapping("/goods")
public class MarketGoodsController {

    @Autowired
    private MarketGoodsService marketGoodsService;

    @ApiLimiting
    @ApiOperation(value = "分享商品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/share")
    public ResultVO<GoodsShareVO> shareGoods(@RequestBody GoodsShareVO goodsShareAO) {
        Long goodsId = marketGoodsService.shareGoods(UserContext.get().getUserId(), goodsShareAO);
        return Result.success(new GoodsShareVO(goodsId));
    }

    @ApiLimiting
    @ApiOperation(value = "商品列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @GetMapping("/list")
    public ResultVO<PageDTO<GoodsInfoVO>> listGoods(PageQuery pageQuery) {
        if (pageQuery.getUserId() == null) {
            pageQuery.setUserId(UserContext.get().getUserId());
        }
        PageDTO<GoodsInfoVO> goodsPage = marketGoodsService.getGoodsPage(UserContext.get().getUserId(), pageQuery);
        return Result.success(goodsPage);
    }

    @ApiLimiting
    @ApiOperation(value = "下架商品")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @DeleteMapping("/{goodsId}/off")
    public ResultVO<?> offGoods(@PathVariable("goodsId") Long goodsId) {
        marketGoodsService.offGoods(UserContext.get().getUserId(), goodsId);
        return Result.success();
    }

    @ApiLimiting
    @ApiOperation(value = "导入商品到本地")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/{goodsId}/import")
    public ResultVO<GoodsInfoVO> importGoods(@PathVariable("goodsId") Long goodsId) {
        GoodsInfoVO goodsInfoVO = marketGoodsService.importGoods(UserContext.get().getUserId(), goodsId);
        return Result.success(goodsInfoVO);
    }
}
