package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * LikeCountUserDO class<br/>
 *
 * @author hdonghong
 * @since 2019/06/08
 */
@TableName("`like_count_user`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LikeCountUserDO extends BaseDO {

    private static final long serialVersionUID = -6012198102962260914L;

    @TableId
    private Long userId;

    private Integer likeCount;
}
