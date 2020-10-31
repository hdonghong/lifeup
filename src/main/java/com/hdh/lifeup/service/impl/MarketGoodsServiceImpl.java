package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.hdh.lifeup.dao.MarketGoodsImportationMapper;
import com.hdh.lifeup.dao.MarketGoodsMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.constant.CommonConst;
import com.hdh.lifeup.model.domain.MarketGoodsDO;
import com.hdh.lifeup.model.domain.MarketGoodsImportationDO;
import com.hdh.lifeup.model.dto.MarketGoodsDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.query.PageQuery;
import com.hdh.lifeup.model.vo.GoodsInfoVO;
import com.hdh.lifeup.model.vo.GoodsShareVO;
import com.hdh.lifeup.redis.LikeKey;
import com.hdh.lifeup.service.LikeService;
import com.hdh.lifeup.service.MarketGoodsService;
import com.hdh.lifeup.service.UserInfoService;
import com.hdh.lifeup.util.sensitive.SensitiveFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * MarketGoodsServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/10/18
 */
@Service
public class MarketGoodsServiceImpl implements MarketGoodsService {

    @Autowired
    private MarketGoodsMapper marketGoodsMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MarketGoodsImportationMapper marketGoodsImportationMapper;
    @Autowired
    private LikeService likeService;

    @Override
    public MarketGoodsDTO getOne(Long goodsId) {
        MarketGoodsDO marketGoodsDO = marketGoodsMapper.selectById(goodsId);
        return MarketGoodsDTO.from(marketGoodsDO, MarketGoodsDTO.class);
    }

    @Override
    public Long shareGoods(Long userId, GoodsShareVO goodsShareVO) {
        // 敏感词过滤
        goodsShareVO.setGoodsName(SensitiveFilter.filter(goodsShareVO.getGoodsName()))
            .setGoodsDesc(SensitiveFilter.filter(goodsShareVO.getGoodsDesc()));

        MarketGoodsDO marketGoodsDO = new MarketGoodsDO();
        BeanUtils.copyProperties(goodsShareVO, marketGoodsDO);
        marketGoodsDO.setUserId(userId);
        if (goodsShareVO.getGoodsId() == null) {
            return createGoods(marketGoodsDO);
        }
        return updateGoods(marketGoodsDO);
    }

    @Override
    public PageDTO<GoodsInfoVO> getGoodsPage(Long userId, PageQuery pageQuery) {
        QueryWrapper<MarketGoodsDO> wrapper = new QueryWrapper<MarketGoodsDO>()
            .eq("create_source", pageQuery.getCreateSource());
        // 按照时间倒序
        if (CommonConst.RankRule.CREATE_TIME_DESC.equals(pageQuery.getRank())) {
            wrapper.orderByDesc("create_time");
        }
        // 过滤条件
        if (CommonConst.QueryFilter.ONLY_THE_USER.equals(pageQuery.getFilter())) {
            wrapper.eq("user_id", userId);
        }
        // 查询
        IPage<MarketGoodsDO> marketGoodsDOIPage = marketGoodsMapper.selectPage(
            new Page<>(pageQuery.getCurrentPage(), pageQuery.getSize()), wrapper);
        // 模型转换
        PageDTO<GoodsInfoVO> goodsDTOPage = PageDTO.createFreely(marketGoodsDOIPage, GoodsInfoVO.class);
        assembleGoodsList(goodsDTOPage.getList(), userId);
        // 排序
        if (CommonConst.RankRule.LIKE_COUNT_FIRST.equals(pageQuery.getRank())) {
            goodsDTOPage.getList()
                .sort((g1, g2) -> g2.getLikeCount() - g1.getLikeCount());
        }
        return goodsDTOPage;
    }

    @Override
    public void offGoods(Long userId, Long goodsId) {
        MarketGoodsDO marketGoodsDO = marketGoodsMapper.selectById(goodsId);
        if (marketGoodsDO == null) {
            throw new GlobalException(CodeMsgEnum.PARAMETER_ERROR);
        }
        if (!marketGoodsDO.getUserId().equals(userId) && !userId.equals(1043741331927199746L)) {
            throw new GlobalException(CodeMsgEnum.ACCESS_ILLEGAL);
        }
        marketGoodsMapper.deleteById(goodsId);
    }

    @Override
    public void importGoods(Long userId, Long goodsId) {
        QueryWrapper<MarketGoodsImportationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.eq("goods_id", goodsId)
            .eq("user_id", userId);
        MarketGoodsImportationDO marketGoodsImportationDO = marketGoodsImportationMapper.selectOne(queryWrapper);
        if (marketGoodsImportationDO != null) {
            return;
        }
        marketGoodsImportationDO = new MarketGoodsImportationDO()
            .setGoodsId(goodsId)
            .setUserId(userId);
        marketGoodsImportationMapper.insert(marketGoodsImportationDO);
    }

    /**
     * 给商品列表装配额外信息
     * 包括，创建者用户数据、点赞数据、导入数据、排序
     * @param goodsInfoVOList
     */
    private void assembleGoodsList(List<GoodsInfoVO> goodsInfoVOList, Long currUserId) {
        Map<Long, UserInfoDTO> userInfoTempCache = Maps.newHashMap();
        goodsInfoVOList.forEach(goodsInfoVO -> {
            // 创建者用户数据
            goodsInfoVO.setIsMine(goodsInfoVO.getUserId().equals(currUserId));
            goodsInfoVO.setCreator(getUserInfo(goodsInfoVO.getUserId(), userInfoTempCache));
            // 点赞数据
            int like = likeService.isLike(LikeKey.GOODS, goodsInfoVO.getGoodsId(), currUserId);
            int goodsLikeCount = likeService.getGoodsLikeCount(goodsInfoVO.getGoodsId());
            goodsInfoVO.setIsLike(like == 1);
            goodsInfoVO.setLikeCount(goodsLikeCount);
            // 导入数据
            QueryWrapper<MarketGoodsImportationDO> wrapper = new QueryWrapper<>();
            wrapper.eq("goods_id", goodsInfoVO.getGoodsId());
            Integer importCount = marketGoodsImportationMapper.selectCount(wrapper);
            goodsInfoVO.setImportCount(importCount);
        });
    }

    /**
     * 用户信息临时缓冲区
     * @param userId
     * @param userInfoTempCache
     * @return
     */
    private UserInfoDTO getUserInfo(Long userId, Map<Long, UserInfoDTO> userInfoTempCache) {
        UserInfoDTO userInfoDTO;
        if ((userInfoDTO = userInfoTempCache.get(userId)) != null) {
            return userInfoDTO;
        }
        userInfoDTO = userInfoService.getOneSafely(userId);
        userInfoTempCache.put(userId, userInfoDTO);
        return userInfoDTO;
    }

    private Long createGoods(MarketGoodsDO marketGoodsDO) {
        // 参数校验
        boolean isParamError = StringUtils.isEmpty(marketGoodsDO.getGoodsName())
            && StringUtils.isEmpty(marketGoodsDO.getGoodsPrice());
        if (isParamError) {
            throw new GlobalException(CodeMsgEnum.PARAMETER_ERROR);
        }
        // 默认来源是国内
        if (marketGoodsDO.getCreateSource() == null) {
            marketGoodsDO.setCreateSource(CommonConst.CreateSource.CHINA);
        }
        // 入库
        marketGoodsMapper.insert(marketGoodsDO);
        return marketGoodsDO.getGoodsId();
    }

    private Long updateGoods(MarketGoodsDO updatedGoods) {
        // 参数校验
        MarketGoodsDO marketGoodsDO = marketGoodsMapper.selectById(updatedGoods.getGoodsId());
        if (marketGoodsDO == null || !marketGoodsDO.getUserId().equals(updatedGoods.getUserId())) {
            throw new GlobalException(CodeMsgEnum.PARAMETER_ERROR);
        }
        // 更新
        marketGoodsMapper.updateById(updatedGoods);
        return updatedGoods.getGoodsId();
    }
}
