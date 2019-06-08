package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ReportTypeDO class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@TableName("`report_type`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportTypeDO extends BaseDO  {
    private static final long serialVersionUID = -4962921566041082170L;

    @TableId
    private Long reportTypeId;

    private String typeName;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
