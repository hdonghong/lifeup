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

    private Integer hasComplete;

    private Integer hasReceive;

    private LocalDateTime completeTime;

    private Long clientAchievementId;

    private Long userId;
}
