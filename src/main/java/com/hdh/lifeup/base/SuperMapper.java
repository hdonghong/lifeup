package com.hdh.lifeup.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * SuperMapper interface<br/>
 * 程序的通用mapper类
 * @author hdonghong
 * @since 2018/08/13
 */
public interface SuperMapper<T extends BaseDO> extends BaseMapper<T> {
}
