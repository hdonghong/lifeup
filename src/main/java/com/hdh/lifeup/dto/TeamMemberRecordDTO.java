package com.hdh.lifeup.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.TeamMemberRecordDO;
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
public class TeamMemberRecordDTO extends BaseDTO<TeamMemberRecordDO> {

    private static final long serialVersionUID = 3280320349458708093L;

    private Long memberRecordId;

    private Long teamRecordId;

    private Long teamId;

    private String teamTitle;

    private Long userId;

    private String userActivity;

    private Integer activityIcon;

    private LocalDateTime createTime;


}
