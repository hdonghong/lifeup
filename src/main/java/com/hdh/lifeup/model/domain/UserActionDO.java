package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserActionDO class<br/>
 *
 * @author hdonghong
 * @since 2020/08/09
 */
@TableName("`user_action`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserActionDO extends BaseDO {
    private static final long serialVersionUID = 8947915997124039376L;


    @TableId
    private Long actionId;

    /**
     * 行为描述，默认语言，中文
     */
    private String actionDesc;

    private Integer actionScore;

    private Integer scoreLimit;
}
