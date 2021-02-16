package com.hdh.lifeup.model.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * BatchQuery class<br/>
 * 批量查询请求
 * @author hdonghong
 * @since 2021/01/10
 */
@Data
@Accessors(chain = true)
public class BatchQuery<T> {

    /**
     * 客户端自定义的唯一标识
     * 用户每批各个元素间做区分
     */
    private String uuid;

    /**
     * 元素
     */
    private T t;
}
