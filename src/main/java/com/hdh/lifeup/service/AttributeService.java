package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.AttributeDTO;
import lombok.NonNull;

/**
 * AttributeService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
public interface AttributeService {

    AttributeDTO insert(@NonNull AttributeDTO attributeDTO);

    /**
     * 通过userid查看自己人物详细属性
     * @param userId 主键
     * @return 属性
     */
    AttributeDTO getByUserId(Long userId);

    AttributeDTO update(@NonNull AttributeDTO attributeDTO);
}
