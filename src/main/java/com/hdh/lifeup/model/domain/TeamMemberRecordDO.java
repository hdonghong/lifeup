package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import com.hdh.lifeup.model.constant.CommonConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * TeamMemberRecordDO class<br/>
 * 成员签到动态
 * @author hdonghong
 * @since 2018/09/11
 */
@TableName("`team_member_record`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamMemberRecordDO extends BaseDO {

    private static final long serialVersionUID = 3280320349458708093L;

    @TableId
    private Long memberRecordId;

    private Long teamRecordId;

    private String teamTitle;

    private Long teamId;

    private Long userId;

    private String userActivity;

    private Integer activityIcon;

    private String activityImages;

    private LocalDateTime createTime;

    /**
     * 团队团建来源 ： 国内/海外
     * @see CommonConst.CreateSource
     */
    private Integer createSource;

    /**
     * 创建者当地时区
     */
    private String localTimeZone;

    /**
     * 当地时间
     */
    private LocalDateTime localCreateTime;

}
