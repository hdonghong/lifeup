package com.hdh.lifeup.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * LikeVO class<br/>
 *
 * @author hdonghong
 * @since 2019/06/08
 */
@ApiModel("赞的vo类")
@Data
@Accessors(chain = true)
public class LikeVO {

    private Integer likeCount;
}
