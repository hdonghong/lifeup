package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.AttributeDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * AttributeDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AttributeDTO extends BaseDTO<AttributeDO> {

    private static final long serialVersionUID = -2424390811933753913L;
    
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

}
