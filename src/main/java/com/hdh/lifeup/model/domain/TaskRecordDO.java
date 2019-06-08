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
 * TaskRecordDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@TableName("`task_record`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TaskRecordDO extends BaseDO {
    private static final long serialVersionUID = 6077985702506110446L;

    @TableId
    private Long recordId;

    /** 用户预计的首次到期完成时间 */
    private LocalDateTime expireTime;

    /** 提醒预备事项时间 */
    private LocalDateTime remindTime;

    /** 本次签到or完成时间 */
    private LocalDateTime recordCompleteTime;

    /** 本次事项的完成状态， 默认0未完成；1完成。当完成时，如果事项还有下一次，需要同时生成新的表记录，计算出下一次的expiredTime和remindTime */
    private Integer recordStatus;

    /** 关联 */
    private Integer taskId;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
