package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDO;
import com.hdh.lifeup.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserAchievementDO class<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserAchievementDTO extends BaseDTO {

    private static final long serialVersionUID = 4317496616548743925L;

    private Long achievementId;

    private Integer hasComplete;

    private Integer hasReceive;

    private LocalDateTime completeTime;

    private Long clientAchievementId;

    private Long userId;

}
