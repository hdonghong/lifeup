package com.hdh.lifeup.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * UserAchievementVO class<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
@Data
public class UserAchievementVO {

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
