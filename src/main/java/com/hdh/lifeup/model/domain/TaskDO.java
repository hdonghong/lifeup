package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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

    /** '奖励的属性名，空格分开 */
    private String rewardAttributes;

    /** '对应的属性经验值 */
    private Integer rewardExp;

    /** '任务频率，0即不重复，1即1天/次；7即1周/次；30即1个月/次 */
    private Integer taskFrequency;

    /** 是否分享到社区0否；1是 */
    private Integer isShared;

    /** 任务状态：0进行中；1完成；2逾期；3放弃 */
    private Integer taskStatus;

    /** 用户预计的首次到期完成时间（可选，为空表示随时可完成,不用最后通知' */
    private LocalDateTime firstExpireTime;

    /** 首次提醒时间，当首次期间存在时需要小于它； */
    private LocalDateTime firstRemindTime;

    /** 整个事项的完成时间 */
    private LocalDateTime taskCompleteTime;

    /** '关联的用户id' */
    private Long userId;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;

    /** 创建时间 */
    private LocalDateTime createTime;
}
