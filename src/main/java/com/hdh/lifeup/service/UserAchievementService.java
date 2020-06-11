package com.hdh.lifeup.service;

import com.hdh.lifeup.model.ao.UserAchievementAO;
import com.hdh.lifeup.model.dto.UserAchievementDTO;

import java.util.List;

/**
 * UserAchievementService interface<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
public interface UserAchievementService {

    void sync(UserAchievementAO userAchievementAO);

    List<UserAchievementDTO> listAchievements(Long userId, Integer hasComplete);

    void deleteByUserId(Long userId);
}
