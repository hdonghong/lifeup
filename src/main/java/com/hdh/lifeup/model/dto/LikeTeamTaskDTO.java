package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.LikeTeamTaskDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * LikeTeamTaskDTO class<br/>
 * 团队点赞
 * @author hdonghong
 * @since 2020/07/03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LikeTeamTaskDTO extends BaseDTO<LikeTeamTaskDO> {

    private static final long serialVersionUID = 4154659854514017541L;

    private Long likeTeamId;

    private Long teamId;

    private Long userId;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
