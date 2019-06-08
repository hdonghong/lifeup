package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * AttributeDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@TableName("`user_attribute`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AttributeDO extends BaseDO {

    private static final long serialVersionUID = -2424390811933753913L;

    @TableId
    private Long attributeId;

    /** 人物等级 */
    private Integer userGrade;

    /** 人物总经验条 */
    private Integer userExp;

    /** 力量属性 */
    private Integer attributeStrength;

    /** 学识属性 */
    private Integer attributeKnowledge;

    /** 魅力属性 */
    private Integer attributeCharm;

    /** 耐力属性 */
    private Integer attributeEndurance;

    /** 活力属性 */
    private Integer attributeEnergy;

    /** 创造力属性 */
    private Integer attributeCreativity;

    /** 关联 */
    private Long userId;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
