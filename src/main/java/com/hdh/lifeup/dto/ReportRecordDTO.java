package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.ReportRecordDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * ReportRecordDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportRecordDTO extends BaseDTO<ReportRecordDO> {

    private static final long serialVersionUID = -2424390811933753913L;

    private Long reportId;

    @ApiModelProperty(value = "举报类型id")
    @NotNull(message = "举报类型id不能为空")
    private Long reportTypeId;

    @ApiModelProperty(value = "举报人的id")
    private Long reportUserId;

    @ApiModelProperty(value = "举报项，比如team、user、activity等等", example = "team")
    @NotEmpty(message = "举报项不能为空")
    private String reportItem;

    @ApiModelProperty(value = "对应的举报项的id")
    @NotNull(message = "对应的举报项的id不能为空")
    private Long itemId;

    @ApiModelProperty(value = "被举报的人的id")
    @NotNull(message = "被举报的人的id不能为空")
    private Long criminalUserId;

}
