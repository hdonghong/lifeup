package com.hdh.lifeup.base;

import com.hdh.lifeup.dto.PageDTO;

import java.io.Serializable;
import java.util.List;

/**
 * BaseService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
public interface BaseService<DTO extends BaseDTO, ID extends Serializable> {

    /**
     * 根据id获取一个DTO
     * @param id DTO的id
     * @return 结果DTO
     */
    DTO getOne(ID id);

    /**
     * 新增，用于做必要的复杂逻辑
     * @param dto 源DTO类
     * @return 结果DTO
     */
    DTO insert(DTO dto);

    /**
     * 修改，用于做必要的复杂逻辑
     * @param dto 源DTO类
     * @return 结果DTO
     */
    DTO update(DTO dto);

    /**
     * 逻辑删除
     * @param id DTO的id
     * @return 结果DTO
     */
    DTO deleteLogically(ID id);

    /**
     * 删除（真
     * @param id DTO的id
     * @return 结果DTO
     */
    DTO delete(ID id);
}
