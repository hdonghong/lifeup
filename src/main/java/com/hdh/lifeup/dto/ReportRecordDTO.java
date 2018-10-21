package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.ReportRecordDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    private Long reportTypeId;

    @ApiModelProperty(value = "举报人的id")
    private Long reportUserId;

    @ApiModelProperty(value = "举报项，比如team、user、activity等等", example = "team")
    private String reportItem;

    @ApiModelProperty(value = "对应的举报项的id")
    private Long itemId;

    @ApiModelProperty(value = "被举报的人的id")
    private Long criminalUserId;

}
