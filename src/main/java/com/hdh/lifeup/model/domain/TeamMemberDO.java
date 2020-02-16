package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * TeamMemberDO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
@TableName("`team_member`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamMemberDO extends BaseDO {

    private static final long serialVersionUID = -1879347102581849048L;

    @TableId
    private Long memberId;

    private Long userId;

    private Long teamId;

    private String teamRole;

    private LocalDateTime createTime;


    /**
     * 当地时区
     */
    private String localTimeZone;

    /**
     * 当地时间
     */
    private LocalDateTime localCreateTime;
}
