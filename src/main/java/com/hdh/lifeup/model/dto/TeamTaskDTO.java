package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.constant.TaskConst;
import com.hdh.lifeup.model.domain.TeamTaskDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TeamTaskDO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TeamTaskDTO extends BaseDTO<TeamTaskDO> {

    private static final long serialVersionUID = 6742400261790303176L;

    private Long teamId;

    private String teamTitle;

    private String teamDesc;

    private String teamHead;

    private List<String> rewardAttrs;

    private Integer rewardExp;

    /** 频率，0不重复，7则是每周重复一次；服务端最大数值为65525 */
    private Integer teamFreq;

    /** 开始（截至）日期，默认为第一次开始签到时间的日期-1，比如开始签到时间，不知道怎么叙述更好，来个文案，(/▽＼) */
    private LocalDate startDate;

    /** 开始签到的时间，必须开始后才能签到，与endTime在同一天，不填写的话默认为当天0点 */
    private LocalDateTime firstStartTime;

    /** 结束签到的时间，必须开始后才能签到，与startTime在同一天，不填写的话默认为当天23.59.59点 */
    private LocalDateTime firstEndTime;

    /** 团队终止时间 */
    private LocalDateTime completeTime;

    /** 团队状态，0进行中；2完成；3放弃 */
    private Integer teamStatus;

    private Long userId;

    /** 金币 取值区间[0, 99]*/
    private Integer coin = 0;

    /** 金币随机量 取值区间[0, 99]*/
    private Integer coinVariable = 0;

    /** 团队活跃度 */
    private Integer teamRank;

    private LocalDateTime createTime;


    /**
     * 团队团建来源 ： 国内/海外
     * @see TaskConst.CreateSource
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

    /**
     * 子任务列表
     */
    private List<TeamSubTaskDTO> subTaskList;

    private Integer likeCount;

    private Integer isLike;

    @Override
    public TeamTaskDO toDO(Class<TeamTaskDO> doClass) {
        try {
            TeamTaskDO taskDO = doClass.newInstance();
            BeanUtils.copyProperties(this, taskDO, "rewardAttrs");
            if (this.rewardAttrs != null && this.rewardAttrs.size() > 0) {
                taskDO.setRewardAttrs(JsonUtil.toJson(this.rewardAttrs));
            }
            return taskDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <DTO extends BaseDTO> DTO from(TeamTaskDO aDO) {
        TeamTaskDTO teamDTO = new TeamTaskDTO();
        BeanUtils.copyProperties(aDO, teamDTO, "rewardAttrs");
        teamDTO.setRewardAttrs(JsonUtil.jsonToList(aDO.getRewardAttrs(), String.class));
        return (DTO) teamDTO;
    }
}
