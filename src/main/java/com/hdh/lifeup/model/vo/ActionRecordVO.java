package com.hdh.lifeup.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ActionRecordVO class<br/>
 *
 * @author hdonghong
 * @since 2020/08/03
 */
@ApiModel("用户行为记录")
@Data
@Accessors(chain = true)
public class ActionRecordVO {

    @ApiModelProperty("id")
    private Long actionId;

    @ApiModelProperty("来源，格式：app_版本号")
    private String actionSource;
}
