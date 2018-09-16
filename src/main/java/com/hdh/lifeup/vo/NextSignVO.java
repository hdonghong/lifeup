package com.hdh.lifeup.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * NextSignVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/03
 */
@ApiModel("即将要签到的信息VO类")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class NextSignVO implements Serializable {

    private static final long serialVersionUID = -2991679595328300361L;

    private Long teamId;

    private Long teamRecordId;

    @ApiModelProperty(value = "团队标题，也是事项标题")
    private String teamTitle;

    @ApiModelProperty("下一次开始签到的时间")
    private LocalDateTime nextStartTime;

    @ApiModelProperty("下一次结束签到的时间")
    private LocalDateTime nextEndTime;
}
