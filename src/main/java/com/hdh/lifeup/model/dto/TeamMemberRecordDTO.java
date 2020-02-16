package com.hdh.lifeup.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.constant.TaskConst;
import com.hdh.lifeup.model.domain.TeamMemberRecordDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<String> activityImages;

    private LocalDateTime createTime;

    /**
     * 团队团建来源 ： 国内/海外
     * @see TaskConst.CreateSource
     */
    private Integer createSource;

    /**
     * 当地时区
     */
    private String localTimeZone;

    /**
     * 当地时间
     */
    private LocalDateTime localCreateTime;

    @Override
    public TeamMemberRecordDO toDO(Class<TeamMemberRecordDO> doClass) {
        try {
            TeamMemberRecordDO teamMemberRecordDO = doClass.newInstance();
            BeanUtils.copyProperties(this, teamMemberRecordDO, "activityImages");
            if (this.activityImages != null && this.activityImages.size() > 0) {
                teamMemberRecordDO.setActivityImages(JsonUtil.toJson(this.activityImages));
            }
            return teamMemberRecordDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <DTO extends BaseDTO> DTO from(TeamMemberRecordDO aDO) {
        TeamMemberRecordDTO teamMemberRecordDTO = new TeamMemberRecordDTO();
        BeanUtils.copyProperties(aDO, teamMemberRecordDTO, "activityImages");
        teamMemberRecordDTO.setActivityImages(JsonUtil.jsonToList(aDO.getActivityImages(), String.class));
        return (DTO) teamMemberRecordDTO;
    }
}
