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
     * 根据条件获取整个列表
     * @param queryCondition 查询条件
     * @param <T> 列表泛型
     * @return 列表
     */
    <T> List<DTO> listByConditions(T queryCondition);

    /**
     * 根据条件获取目标页码数分页 FIXME 返回值待确认
     * @param queryCondition 查询条件
     * @param currPage 目标页码数
     * @param pageSize 页面数据量
     * @param <T> 列表泛型
     * @return 列表分页
     */
    <T> PageDTO<DTO> pageByConditions(T queryCondition, int currPage, int pageSize);

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
