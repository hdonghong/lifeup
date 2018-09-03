package com.hdh.lifeup.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * TeamRecordDO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamRecordDO extends BaseDO {

    private static final long serialVersionUID = -2961319466185911546L;

    @TableId
    private Long teamRecordId;

    private Long teamId;

    private Long userId;

    /** 用户动态 */
    private String userActivity;

    /** 下一次的提醒开始签到的时间 */
    private LocalDateTime nextStartTime;

    private LocalDateTime nextEndTime;

    private LocalDateTime signinTime;

    private LocalDateTime createTime;

}
