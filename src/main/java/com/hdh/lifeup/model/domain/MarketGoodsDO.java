package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import com.hdh.lifeup.model.constant.CommonConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * MarketGoodsDO class<br/>
 * 商品
 * @author hdonghong
 * @since 2020/10/17
 */
@TableName("`market_goods`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MarketGoodsDO extends BaseDO {

    private static final long serialVersionUID = -9073795923982737428L;

    @TableId
    private Long goodsId;

    private String goodsName;

    private String goodsImg;

    private String goodsDesc;

    private Integer goodsPrice;

    private String extendInfo;

    private Long userId;

    /**
     * @see CommonConst.CreateSource
     */
    private Integer createSource;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
