package com.hdh.lifeup.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * RecordDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/16
 */
@Data
@Accessors(chain = true)
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 3280320349458708093L;

    private Long memberRecordId;

    private Long teamRecordId;

    private Long teamId;

    private String teamTitle;

    private Long userId;

    private String nickname;

    private String userHead;

    private String userActivity;

    private Integer activityIcon;

    private LocalDateTime createTime;

}
