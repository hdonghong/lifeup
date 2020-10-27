package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.constant.CommonConst;
import com.hdh.lifeup.model.domain.MarketGoodsDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * MarketGoodsDTO class<br/>
 * 商品
 * @author hdonghong
 * @since 2020/10/18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class MarketGoodsDTO extends BaseDTO<MarketGoodsDO> {

    private static final long serialVersionUID = -9073795923982737428L;

    private Long goodsId;

    private String goodsName;

    private String goodsImg;

    private String goodsDesc;

    private Integer goodsPrice;

    private Long userId;

    private String extendInfo;

    /**
     * @see CommonConst.CreateSource
     */
    private Integer createSource;

    /** '创建时间' */
    private LocalDateTime createTime;
}
