package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.ReportTypeDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ReportTypeDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportTypeDTO extends BaseDTO<ReportTypeDO> {
    private static final long serialVersionUID = -4962921566041082170L;

    @ApiModelProperty(value = "举报类型id")
    private Long reportTypeId;

    @ApiModelProperty(value = "举报类型名")
    private String typeName;

}
