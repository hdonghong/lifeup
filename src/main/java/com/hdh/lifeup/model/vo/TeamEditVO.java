package com.hdh.lifeup.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * TeamEditVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/20
 */
@ApiModel("团队可编辑信息的VO类")
@Data
@Accessors(chain = true)
public class TeamEditVO {

    @ApiModelProperty(value = "id，新建时不允许提交")
    @NotEmpty
    private Long teamId;

    @ApiModelProperty(value = "团队标题，也是事项标题")
    private String teamTitle;

    @ApiModelProperty(value = "团队详情，具体描述")
    private String teamDesc;

    @ApiModelProperty(value = "团队头像")
    private String teamHead;

    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coin = 0;

    /** 金币随机量 取值区间[0, 99]*/
    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coinVariable = 0;
}
