package com.hdh.lifeup.model.vo;

import com.hdh.lifeup.model.dto.UserInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TeamDetailVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/13
 */
@ApiModel("团队详情VO")
@Data
@Accessors(chain = true)
public class TeamDetailVO {

    private Long teamId;

    @ApiModelProperty("标题")
    private String teamTitle;

    @ApiModelProperty("描述")
    private String teamDesc;

    @ApiModelProperty("团队头像")
    private String teamHead;

    @ApiModelProperty("奖励的属性")
    private List<String> rewardAttrs;

    @ApiModelProperty("奖励的经验值")
    private Integer rewardExp;

    @ApiModelProperty("频率，0不重复，7则是每周重复一次；服务端最大数值为65525")
    private Integer teamFreq;

    @ApiModelProperty("团队开始日期（截止日期）")
    private LocalDate startDate;

    @ApiModelProperty("下一次开始签到的时间")
    private LocalDateTime nextStartTime;

    @ApiModelProperty("下一次结束签到的时间")
    private LocalDateTime nextEndTime;

    @ApiModelProperty("团队完成的时间，未完成时不会传给前端")
    private LocalDateTime completeTime;

    @ApiModelProperty("团队状态，0进行中；2完成；3放弃")
    private Integer teamStatus;

    @ApiModelProperty("团队的拥有者")
    private UserInfoDTO owner;

    @ApiModelProperty("团队成员数量")
    private Integer memberAmount;

    @ApiModelProperty("1已加入 0未加入")
    private Integer isMember;

    @ApiModelProperty("1创建者 0不是")
    private Integer isOwner;

    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coin = 0;

    /** 金币随机量 取值区间[0, 99]*/
    @ApiModelProperty(value = "金币 取值区间[0, 99]")
    private Integer coinVariable = 0;


    /**
     * 创建者当地时区
     */
    @ApiModelProperty(value = "创建者当地时区, 取值从 -12 到 +12")
    private String localTimeZone;


    /**
     * 子任务列表
     */
    @ApiModelProperty(value = "子任务列表")
    private List<String> subTaskList;
}
