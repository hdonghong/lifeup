package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.model.domain.AppVersionDO;
import com.hdh.lifeup.model.dto.AppVersionDTO;
import com.hdh.lifeup.dao.AppVersionMapper;
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
    public AppVersionDTO getLastVersion() {
        List<AppVersionDO> versionDOList = appVersionMapper.selectList(
                new QueryWrapper<AppVersionDO>().orderByDesc("version_id")
        );
        return AppVersionDTO.from(versionDOList.get(0), AppVersionDTO.class);
    }
}
