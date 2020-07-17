package com.hdh.lifeup.model.ao;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserAchievementAO class<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
@Data
public class UserAchievementAO {

    /**
     * 是否已解锁
     */
    private Integer hasComplete;

    /**
     * 是否已经领取
     */
    private Integer hasReceive;

    private LocalDateTime completeTime;

    private Long clientAchievementId;

    private Long userId;
}
