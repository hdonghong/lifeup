package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * LikeMarketGoodsDO class<br/>
 * 商品点赞
 * @author hdonghong
 * @since 2020/07/03
 */
@TableName("`like_market_goods`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LikeMarketGoodsDO extends BaseDO {

    private static final long serialVersionUID = 4154659854514017541L;

    @TableId
    private Long likeGoodsId;

    private Long goodsId;

    private Long userId;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
