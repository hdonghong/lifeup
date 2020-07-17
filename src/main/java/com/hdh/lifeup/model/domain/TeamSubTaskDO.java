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
 * TeamSubTaskDO class<br/>
 * 团队子任务
 * @author hdonghong
 * @since 2020/05/18
 */
@TableName("`team_sub_task`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamSubTaskDO extends BaseDO {

    private static final long serialVersionUID = 896134336847401089L;

    @TableId
    private Long taskId;

    private Long teamId;

    private String taskContent;

    private Integer coin;

    private Integer coinVariable;

    @TableLogic
    private Integer isDel;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
