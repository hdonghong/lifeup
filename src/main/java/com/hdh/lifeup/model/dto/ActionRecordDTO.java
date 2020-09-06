package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.ActionRecordDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * ActionRecordDTO class<br/>
 * 用户行为记录
 * @author hdonghong
 * @since 2020/07/03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ActionRecordDTO extends BaseDTO<ActionRecordDO> {

    private static final long serialVersionUID = -9073795923982737428L;

    private Long actionRecordId;

    private Long actionId;

    private String actionSource;

    private Long userId;

    /**
     * 本次动作关联的业务id，比如点赞团队，则是团队id
     */
    private Long relatedId;

    /**
     * 关联的业务类型，比如：team
     */
    private String relatedType;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
