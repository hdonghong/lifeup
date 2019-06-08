package com.hdh.lifeup.service.impl;

import com.google.common.base.Preconditions;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.domain.ReportRecordDO;
import com.hdh.lifeup.model.dto.ReportRecordDTO;
import com.hdh.lifeup.dao.ReportRecordMapper;
import com.hdh.lifeup.service.ReportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ReportRecordServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Service
public class ReportRecordServiceImpl implements ReportRecordService {

    private ReportRecordMapper reportRecordMapper;

    @Autowired
    public ReportRecordServiceImpl(ReportRecordMapper reportRecordMapper) {
        this.reportRecordMapper = reportRecordMapper;
    }

    @Override
    public boolean insert(ReportRecordDTO reportRecordDTO) {
        Preconditions.checkNotNull(reportRecordDTO, "【新增举报记录】reportRecordDTO不能为空");
        reportRecordDTO.setReportUserId(UserContext.get().getUserId());
        reportRecordMapper.insert(reportRecordDTO.toDO(ReportRecordDO.class));
        return true;
    }

}
