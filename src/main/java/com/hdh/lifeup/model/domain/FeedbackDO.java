package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * FeedbackDO class<br/>
 *
 * @author hdonghong
 * @since 2019/10/17
 */
@TableName("`user_feedback`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FeedbackDO extends BaseDO {

    private static final long serialVersionUID = 7571040252773315956L;

    @TableId
    private Long feedbackId;

    private Long userId;

    /** 针对反馈的子id，实现一颗回复tree */
    private Long replyWhichFeedbackId;

    private String feedbackDesc;

    private String feedbackImages;

    /** 0未处理，1处理中，2已处理，3无法处理 */
    private Integer status;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
