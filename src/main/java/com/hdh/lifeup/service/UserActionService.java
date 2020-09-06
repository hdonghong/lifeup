package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.UserActionDTO;

import java.util.List;

/**
 * UserActionService interface<br/>
 *
 * @author hdonghong
 * @since 2020/08/08
 */
public interface UserActionService {


    /**
     * 获取行为描述表的全部
     * @param language
     * @return
     */
    List<UserActionDTO> listAll(String language);
}
