package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.RedeemCodeDO;
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
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RedeemCodeDTO extends BaseDTO<RedeemCodeDO> {

    private static final long serialVersionUID = 2922768667720679703L;

    private Long codeId;

    /**
     * 格式：lifeup@XXXX
     */
    private String redeemCode;

    private Long userId;

    /** 0，未发放；1，已发放；2，已兑换 */
    private Integer status;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
