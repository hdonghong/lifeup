package com.hdh.lifeup.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserActionRecordDTO class<br/>
 *
 * @author hdonghong
 * @since 2020/08/15
 */
@Data
@Accessors(chain = true)
public class UserActionRecordDTO {

    private Long actionId;

    private Long userId;

    /**
     * 行为描述
     */
    private String actionDesc;

    /**
     * 行为得分
     */
    private Integer actionScore;

    private LocalDateTime createTime;
}
