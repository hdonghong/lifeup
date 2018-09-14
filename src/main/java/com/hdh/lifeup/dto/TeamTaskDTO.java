package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.TeamTaskDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * TeamTaskDO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamTaskDTO extends BaseDTO<TeamTaskDO> {

    private static final long serialVersionUID = 6742400261790303176L;

    private Long teamId;

    private String teamTitle;

    private String teamDesc;

    private String teamHead;

    private List<String> rewardAttrs;

    private Integer rewardExp;

    /** 频率，0不重复，7则是每周重复一次；服务端最大数值为65525 */
    private Integer teamFreq;

    /** 开始（截至）日期，默认为第一次开始签到时间的日期-1，比如开始签到时间，不知道怎么叙述更好，来个文案，(/▽＼) */
    private LocalDate startDate;

    /** 开始签到的时间，必须开始后才能签到，与endTime在同一天，不填写的话默认为当天0点 */
    private LocalTime startTime;

    /** 结束签到的时间，必须开始后才能签到，与startTime在同一天，不填写的话默认为当天23.59.59点 */
    private LocalTime endTime;

    /** 团队状态，0未开始；1进行中；2完成；3放弃 */
    private Integer teamStatus;

    private LocalDateTime completeTime;

    private Long userId;

    private LocalDateTime createTime;
}
