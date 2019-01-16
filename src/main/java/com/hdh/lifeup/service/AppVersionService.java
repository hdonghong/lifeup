package com.hdh.lifeup.service;

import com.hdh.lifeup.base.BaseService;
import com.hdh.lifeup.domain.AppVersionDO;
import com.hdh.lifeup.dto.AppVersionDTO;

/**
 * AppVersionService interface<br/>
 * App版本
 * @author hdonghong
 * @since 2019/01/16
 */
public interface AppVersionService extends BaseService<AppVersionDTO, AppVersionDO> {

    AppVersionDTO getLastVersion();
}
