package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.TaskRecordDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * TaskRecordDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskRecordDTO extends BaseDTO<TaskRecordDO> {
    private static final long serialVersionUID = 6077985702506110446L;

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

}
