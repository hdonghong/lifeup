package com.hdh.lifeup.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.TaskDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TaskDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TaskDTO extends BaseDTO<TaskDO> {
    private static final long serialVersionUID = 495764481739950903L;

    private Long taskId;

    /** 任务标题 */
    private String taskTitle;

    /** 备注 */
    private String taskRemark;

    /** '紧急程度1-4' */
    private Integer urgentDegree;

    /** '困难程度，1-4' */
    private Integer difficultDegree;

    /** '奖励的属性名，空格分开 */
    private List<String> rewardAttributes;

    /** '对应的属性经验值 */
    private Integer rewardExp;

    /** '任务频率，0即不重复，1即1天/次；7即1周/次；30即1个月/次 */
    private Integer taskFrequency;

    /** 是否分享到社区0否；1是 */
    private Integer isShared;

    /** 任务状态：0进行中；1完成；2逾期；3放弃 */
    private Integer taskStatus;

    /** 用户预计的首次到期完成时间（可选，为空表示随时可完成,不用最后通知,*/
    private LocalDateTime firstExpireTime;

    /** 首次提醒时间，当首次期间存在时需要小于它； */
    private LocalDateTime firstRemindTime;

    /** 整个事项的完成时间 */
    private LocalDateTime taskCompleteTime;

    /** '关联的用户id' */
    private Long userId;

    /** 创建时间 */
    private LocalDateTime createTime;

    @Override
    public TaskDO toDO(Class<TaskDO> doClass) {
        try {
            TaskDO taskDO = doClass.newInstance();
            BeanUtils.copyProperties(this, taskDO, "rewardAttributes");
            if (this.rewardAttributes != null && this.rewardAttributes.size() > 0) {
                taskDO.setRewardAttributes(new ObjectMapper().writeValueAsString(this.rewardAttributes));
            }
            return taskDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <DTO extends BaseDTO> DTO from(TaskDO aDO) {
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyProperties(aDO, taskDTO, "authTypes");
        try {
            taskDTO.setRewardAttributes(new ObjectMapper().readValue(aDO.getRewardAttributes(), List.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (DTO) taskDTO;
    }
}
