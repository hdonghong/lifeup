package com.hdh.lifeup.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * TaskDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@TableName("`user_task`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TaskDO extends BaseDO {
    private static final long serialVersionUID = 2770588772341546360L;

    @TableId
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

    /** 任务状态：0未完成；1完成；2逾期；3放弃 */
    private Integer taskStatus;

    /** '关联的用户id' */
    private Long userId;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;

    /** 创建时间 */
    private Instant createTime;
}
