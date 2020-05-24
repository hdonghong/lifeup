package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.UserRankDTO;

/**
 * UserRankService interface<br/>
 *
 * @author hdonghong
 * @since 2020/05/18
 */
public interface UserRankService {

    /**
     * 计算用户的排行榜排序值并更新
     * @param userId
     */
    void updateRankValue(Long userId);

    UserRankDTO getRankByUser(Long userId);
}
