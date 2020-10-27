package com.hdh.lifeup.model.vo;

import com.hdh.lifeup.model.constant.CommonConst;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * GoodsInfoVO class<br/>
 * 商品信息模型
 * @author hdonghong
 * @since 2020/10/18
 */
@ApiModel("商品信息模型")
@Data
public class GoodsInfoVO {

    @ApiModelProperty(value = "id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    @ApiModelProperty(value = "商品描述")
    private String goodsDesc;

    @ApiModelProperty(value = "商品价格")
    private Integer goodsPrice;

    @ApiModelProperty(value = "商品扩展参数，可以存放客户端需要的信息，json字符串")
    private String extendInfo;

    /**
     * @see CommonConst.CreateSource
     */
    @ApiModelProperty(value = "商品来源，1国内，2海外，默认国内")
    private Integer createSource;

    @ApiModelProperty(value = "是否属于当前浏览者用户")
    private Boolean isMine;

    @ApiModelProperty("创建者")
    private UserInfoDTO creator;

    private Long userId;

    @ApiModelProperty(value = "是否点赞过")
    private Boolean isLike;

    @ApiModelProperty(value = "点赞量")
    private Integer likeCount;

    @ApiModelProperty(value = "被引入的次数")
    private Integer importCount;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
