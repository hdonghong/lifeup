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
 * TeamRecordDO class<br/>
 * 团队的签到记录情况表
 * @author hdonghong
 * @since 2018/09/02
 */
@TableName("`team_record`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamRecordDO extends BaseDO {

    private static final long serialVersionUID = -2961319466185911546L;

    @TableId
    private Long teamRecordId;

    private Long teamId;

    /** 开始签到的时间 */
    private LocalDateTime nextStartTime;

    /** 结束签到的时间 */
    private LocalDateTime nextEndTime;

    /** 当前已签到的人数 */
    private Integer signNumber;

    @TableLogic
    private Integer isDel;

    private LocalDateTime createTime;

}
