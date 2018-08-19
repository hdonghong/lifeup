package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

/**
 * TaskDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TaskDTO extends BaseDTO {
    private static final long serialVersionUID = 495764481739950903L;

    private Long taskId;

    /** 任务标题 */
    private String taskTitle;

    /** 备注 */
    private String taskRemark;

    /** '紧急程度1-4' */
    private Integer urgentDegree;

    /** '困难程度，1-4' */
    private Integer difficultDegree;

    /** '奖励的属性，是一个json字符串' */
    private String rewardAttributes;

    /** 完成期限，可为空 */
    private Instant taskDeadline;

    /** '任务频率，1即不重复' */
    private Integer taskFrequency;

    /** 是否分享到社区0否；1是 */
    private Integer isShared;

    /** 任务状态：0进行中；1完成；2逾期；3放弃 */
    private Integer taskStatus;

    /** '关联的用户id' */
    private Long userId;

    /** 创建时间 */
    private Instant createTime;
}
