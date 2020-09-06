package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.AppVersionDTO;

/**
 * AppVersionService interface<br/>
 * App版本
 * @author hdonghong
 * @since 2019/01/16
 */
public interface AppVersionService {

    AppVersionDTO getLastVersion(Integer versionType);
}
