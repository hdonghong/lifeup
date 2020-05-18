package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * RedeemCodeDO class<br/>
 * 兑换码
 * @author hdonghong
 * @since 2020/04/06
 */
@TableName("`redeem_code`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RedeemCodeDO extends BaseDO {

    private static final long serialVersionUID = 2922768667720679703L;

    @TableId
    private Long codeId;

    /**
     * 格式：lifeup@XXXX
     */
    private String redeemCode;

    private Long userId;

    /** 0，未发放；1，已发放；2，已兑换 */
    private Integer status;

    @TableLogic
    private Integer isDel;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
