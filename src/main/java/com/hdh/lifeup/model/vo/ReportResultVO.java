package com.hdh.lifeup.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ReportResultVO class<br/>
 * 举报结果模型
 * @author hdonghong
 * @since 2020/10/18
 */
@ApiModel("举报结果模型")
@Data
@Accessors(chain = true)
public class ReportResultVO {

    @ApiModelProperty(value = "数据是否已被删除")
    private Boolean isDel;

}
