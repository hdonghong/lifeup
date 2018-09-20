package com.hdh.lifeup.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ActivityVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/18
 */
@ApiModel("动态VO类")
@Data
@Accessors(chain = true)
public class ActivityVO {

    private String activity;

    private List<String> activityImages;

}
