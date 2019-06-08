package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.ReportTypeDTO;

import java.util.List;

/**
 * ReportTypeService interface<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
public interface ReportTypeService {

    /** 获取所有的举报类型 */
    List<ReportTypeDTO> listAll();
}
