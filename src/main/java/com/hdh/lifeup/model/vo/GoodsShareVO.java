package com.hdh.lifeup.model.vo;

import com.hdh.lifeup.model.constant.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GoodsShareVO class<br/>
 * 商品分享请求
 * @author hdonghong
 * @since 2020/10/18
 */
@ApiModel("商品信息分享模型")
@Data
public class GoodsShareVO {

    @ApiModelProperty(value = "id，新建时不允许提交")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称，创建时必传或者需要更新该字段时必传")
    private String goodsName;

    @ApiModelProperty(value = "商品图片，创建时必传或者需要更新该字段时必传")
    private String goodsImg;

    @ApiModelProperty(value = "商品描述，创建或者需要更新该字段时必传")
    private String goodsDesc;

    @ApiModelProperty(value = "商品价格，创建时必传或者需要更新该字段时必传")
    private Integer goodsPrice;

    @ApiModelProperty(value = "商品扩展参数，可以存放客户端需要的信息，json字符串，创建或者需要更新该字段时必传")
    private String extendInfo;

    /**
     * @see CommonConst.CreateSource
     */
    @ApiModelProperty(value = "商品来源，1国内，2海外，默认国内，创建或者需要更新该字段时必传")
    private Integer createSource;

    public GoodsShareVO() {
    }

    public GoodsShareVO(Long goodsId) {
        this.goodsId = goodsId;
    }
}
