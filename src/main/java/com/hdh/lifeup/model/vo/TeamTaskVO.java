package com.hdh.lifeup.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TeamTaskVO class<br/>
 * 新建团队的VO信息类
 * @author hdonghong
 * @since 2018/09/02
 */
@ApiModel("团队信息VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamTaskVO implements Serializable {

    private static final long serialVersionUID = 6742400261790303176L;

    @ApiModelProperty(value = "id，新建时不允许提交")
    private Long teamId;

    @ApiModelProperty(value = "团队标题，也是事项标题")
    private String teamTitle;

    @ApiModelProperty(value = "团队详情，具体描述")
    private String teamDesc;

    @ApiModelProperty(value = "属性json串")
    private List<String> rewardAttrs;

    @ApiModelProperty(value = "经验value")
    private Integer rewardExp;

    @ApiModelProperty(value = "频率，0不重复，7则是每周重复一次；服务端最大数值为65525")
    private Integer teamFreq;

    /** 开始签到的时间，必须开始后才能签到，与endTime在同一天，不填写的话默认为当天0点 */
    @ApiModelProperty(value = "第一次开始签到的时间，必填")
    private LocalDateTime firstStartTime;

    /** 结束签到的时间，必须开始后才能签到，与startTime在同一天，不填写的话默认为当天23.59.59点 */
    @ApiModelProperty(value = "第一次结束签到的时间，可选，慢于firstStartTime出现，日期数值上 == 开始签到时间的日期")
    private LocalDateTime firstEndTime;

    @ApiModelProperty(value = "团队开始日期（或者说是截止加入的日期）可选，慢于firstStartTime出现，日期数值上 < 开始签到时间的日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "团队头像")
    private String teamHead;

    /** 金币 取值区间[0, 99]*/
    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coin = 0;

    /** 金币随机量 取值区间[0, 99]*/
    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coinVariable = 0;

}
