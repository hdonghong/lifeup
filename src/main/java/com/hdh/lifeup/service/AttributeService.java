package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.dto.AttributeDTO;

/**
 * AttributeService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
public interface AttributeService extends BaseService<AttributeDTO, Long> {

    /**
     * 通过userid查看自己人物详细属性
     * @param userId 主键
     * @return 属性
     */
    AttributeDTO getByUserId(Long userId);
}
