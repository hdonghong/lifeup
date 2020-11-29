package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.ActionRecordDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserActionRecordDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ActionRecordService interface<br/>
 *
 * @author hdonghong
 * @since 2020/08/08
 */
public interface ActionRecordService {


    void reportAction(ActionRecordDTO actionRecordDTO);

    /**
     * 获取用户行为记录
     * @param language
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageDTO<UserActionRecordDTO> pageByUser(String language, Long userId, Integer pageNum, Integer pageSize);

    /**
     * 安装action分组
     * @param language
     * @param userId
     * @return
     */
    List<UserActionRecordDTO> listGroupByAction(String language, Long userId);

    /**
     * 获取指定用户在一段时间内的行为总分
     * @param userId
     * @param from
     * @param to
     * @return
     */
    Integer getTotalScore(Long userId, LocalDateTime from, LocalDateTime to);
}
