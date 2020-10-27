package com.hdh.lifeup.service;

import com.hdh.lifeup.model.domain.MarketGoodsDO;
import com.hdh.lifeup.model.dto.MarketGoodsDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.query.PageQuery;
import com.hdh.lifeup.model.vo.GoodsInfoVO;
import com.hdh.lifeup.model.vo.GoodsShareVO;

/**
 * MarketGoodsService class<br/>
 * 商品服务
 * @author hdonghong
 * @since 2020/10/18
 */
public interface MarketGoodsService {

    MarketGoodsDTO getOne(Long goodsId);

    /**
     * 分享商品
     * 将用户的本地商品数据分享到平台上
     * @return
     */
    Long shareGoods(Long userId, GoodsShareVO goodsShareAO);

    /**
     * 获取商品列表分页
     * @param pageQuery
     * @return
     */
    PageDTO<GoodsInfoVO> getGoodsPage(Long userId, PageQuery pageQuery);

    /**
     * 下架商品
     * @param userId
     * @param goodsId
     */
    void offGoods(Long userId, Long goodsId);

    /**
     * 导入商品
     * @param userId
     * @param goodsId
     */
    void importGoods(Long userId, Long goodsId);
}
