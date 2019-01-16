package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.domain.AppVersionDO;
import com.hdh.lifeup.dto.AppVersionDTO;
import com.hdh.lifeup.mapper.AppVersionMapper;
import com.hdh.lifeup.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AppVersionServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2019/01/16
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

    private AppVersionMapper appVersionMapper;

    @Autowired
    public AppVersionServiceImpl(AppVersionMapper appVersionMapper) {
        this.appVersionMapper = appVersionMapper;
    }

    @Override
    public AppVersionDTO getOne(AppVersionDO appVersionDO) {
        return null;
    }

    @Override
    public AppVersionDTO insert(AppVersionDTO dto) {
        return null;
    }

    @Override
    public AppVersionDTO update(AppVersionDTO dto) {
        return null;
    }

    @Override
    public AppVersionDTO deleteLogically(AppVersionDO appVersionDO) {
        return null;
    }

    @Override
    public AppVersionDTO delete(AppVersionDO appVersionDO) {
        return null;
    }

    @Override
    public AppVersionDTO getLastVersion() {
        List<AppVersionDO> versionDOList = appVersionMapper.selectList(
                new QueryWrapper<AppVersionDO>().orderByDesc("version_id")
        );
        return AppVersionDTO.from(versionDOList.get(0), AppVersionDTO.class);
    }
}
