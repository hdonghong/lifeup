package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * MarketGoodsImportationDO class<br/>
 * 商品
 * @author hdonghong
 * @since 2020/10/17
 */
@TableName("`market_goods_importation`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MarketGoodsImportationDO extends BaseDO {

    private static final long serialVersionUID = -9073795923982737428L;

    @TableId
    private Long importId;

    private Long goodsId;

    private Long userId;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
