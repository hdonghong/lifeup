package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.UserActionDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserActionDTO class<br/>
 * 用户行为描述
 * @author hdonghong
 * @since 2020/07/03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserActionDTO extends BaseDTO<UserActionDO> {

    private static final long serialVersionUID = -9073795923982737428L;

    private Long actionId;

    /**
     * 行为描述
     */
    private String actionDesc;

    private Integer actionScore;

    private Integer scoreLimit;
}
