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
 * UserAchievementDO class<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`user_achievement`")
public class UserAchievementDO extends BaseDO {

    private static final long serialVersionUID = 4317496616548743925L;

    @TableId
    private Long achievementId;

    private Integer hasComplete;

    private Integer hasReceive;

    private LocalDateTime completeTime;

    private Long clientAchievementId;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDel;
}
