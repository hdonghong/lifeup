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
 * LikeTeamTaskDO class<br/>
 * 团队点赞
 * @author hdonghong
 * @since 2020/07/03
 */
@TableName("`like_team_task`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LikeTeamTaskDO extends BaseDO {

    private static final long serialVersionUID = 4154659854514017541L;

    @TableId
    private Long likeTeamId;

    private Long teamId;

    private Long userId;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
