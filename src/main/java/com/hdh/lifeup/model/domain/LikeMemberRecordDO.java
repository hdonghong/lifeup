package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * LikeMemberRecordDO class<br/>
 *
 * @author hdonghong
 * @since 2019/06/08
 */
@TableName("`like_member_record`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LikeMemberRecordDO extends BaseDO {
    private static final long serialVersionUID = 1836760932957359007L;

    @TableId
    private Long memberRecordId;

    private Long userId;
}
