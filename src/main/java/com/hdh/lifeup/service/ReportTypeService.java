package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.domain.ReportTypeDO;
import com.hdh.lifeup.dto.ReportTypeDTO;

import java.util.List;

/**
 * ReportTypeService interface<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
public interface ReportTypeService extends BaseService<ReportTypeDTO, ReportTypeDO> {

    List<ReportTypeDTO> listAll();
}
