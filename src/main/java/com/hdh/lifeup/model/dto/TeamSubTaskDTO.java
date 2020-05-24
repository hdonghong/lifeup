package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.TeamSubTaskDO;
import com.hdh.lifeup.model.domain.UserRankDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * TeamSubTaskDTO class<br/>
 * 团队子任务
 * @author hdonghong
 * @since 2020/05/17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamSubTaskDTO extends BaseDTO<TeamSubTaskDO> {

    private static final long serialVersionUID = 2922768667720679703L;

    private Long taskId;

    private Long teamId;

    private String taskContent;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
