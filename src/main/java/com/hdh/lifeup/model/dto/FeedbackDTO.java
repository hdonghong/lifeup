package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.FeedbackDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * FeedbackDTO class<br/>
 *
 * @author hdonghong
 * @since 2019/10/17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FeedbackDTO extends BaseDTO<FeedbackDO> {

    private static final long serialVersionUID = 831188192086355454L;

    private Long feedbackId;

    private Long userId;

    /** 针对反馈的子id，实现一颗回复tree */
    private Long replyWhichFeedbackId;

    private String feedbackDesc;

    private List<String> feedbackImages;

    /** 0未处理，1处理中，2已处理，3无法处理 */
    private Integer status;

    public void setFeedbackImages(String feedbackImages) {
        this.feedbackImages = feedbackImages == null ?
                new ArrayList<>() : JsonUtil.jsonToList(feedbackImages, String.class);
    }

}
