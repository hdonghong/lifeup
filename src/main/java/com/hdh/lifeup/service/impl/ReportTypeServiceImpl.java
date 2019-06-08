package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.ReportTypeMapper;
import com.hdh.lifeup.model.dto.ReportTypeDTO;
import com.hdh.lifeup.service.ReportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ReportTypeServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Service
public class ReportTypeServiceImpl implements ReportTypeService {

    private ReportTypeMapper reportTypeMapper;

    @Autowired
    public ReportTypeServiceImpl(ReportTypeMapper reportTypeMapper) {
        this.reportTypeMapper = reportTypeMapper;
    }

    @Override
    public List<ReportTypeDTO> listAll() {
        return reportTypeMapper.selectList(new QueryWrapper<>())
                               .stream()
                               .map(typeDO -> ReportTypeDTO.from(typeDO, ReportTypeDTO.class))
                               .collect(Collectors.toList());
    }
}
