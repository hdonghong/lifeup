package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.ReportRecordDTO;
import com.hdh.lifeup.model.vo.ReportResultVO;

/**
 * ReportRecordService interface<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
public interface ReportRecordService {

    ReportResultVO insert(ReportRecordDTO reportRecordDTO);
}
