package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.dao.UserAchievementMapper;
import com.hdh.lifeup.model.ao.UserAchievementAO;
import com.hdh.lifeup.model.domain.UserAchievementDO;
import com.hdh.lifeup.model.dto.UserAchievementDTO;
import com.hdh.lifeup.service.AsyncTaskService;
import com.hdh.lifeup.service.UserAchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hdh.lifeup.model.constant.BizTypeConst.USER_ACHIEVEMENT;
import static com.hdh.lifeup.model.enums.ActionEnum.UNLOCK_ACHIEVEMENT;

/**
 * UserAchievementServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2019/12/28
 */
@Slf4j
@Service
public class UserAchievementServiceImpl implements UserAchievementService {

    @Resource
    private UserAchievementMapper userAchievementMapper;
    @Resource
    private AsyncTaskService asyncTaskService;

    @Override
    public void sync(UserAchievementAO userAchievementAO) {
        Preconditions.checkNotNull(userAchievementAO);
        UserAchievementDO userAchievementDO = new UserAchievementDO();
        BeanUtils.copyProperties(userAchievementAO, userAchievementDO);
        QueryWrapper<UserAchievementDO> achievementQueryWrapper = new QueryWrapper<UserAchievementDO>()
                .eq("user_id", userAchievementDO.getUserId())
                .eq("client_achievement_id", userAchievementDO.getClientAchievementId());
        Integer count = userAchievementMapper.selectCount(achievementQueryWrapper);
        if (count == null || count == 0) {
            userAchievementMapper.insert(userAchievementDO);
            return;
        }
        userAchievementMapper.update(userAchievementDO, achievementQueryWrapper);
        if (Objects.equals(1, userAchievementAO.getHasComplete())) {
            asyncTaskService.reportAction(userAchievementAO.getUserId(), UNLOCK_ACHIEVEMENT, userAchievementDO.getAchievementId(), USER_ACHIEVEMENT);
        }

    }

    @Override
    public List<UserAchievementDTO> listAchievements(Long userId, Integer hasComplete) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(hasComplete);

        QueryWrapper<UserAchievementDO> achievementQueryWrapper = new QueryWrapper<UserAchievementDO>()
                .eq("user_id", userId)
                .eq("has_complete", hasComplete);
        List<UserAchievementDO> userAchievementDOList = userAchievementMapper.selectList(achievementQueryWrapper);
        return userAchievementDOList.stream()
                .map(userAchievementDO -> UserAchievementDTO.from(userAchievementDO, UserAchievementDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByUserId(Long userId) {
        QueryWrapper<UserAchievementDO> achievementQueryWrapper = new QueryWrapper<UserAchievementDO>()
                .eq("user_id", userId);
        Integer count = userAchievementMapper.selectCount(achievementQueryWrapper);
        if (count == null || count == 0) {
            return;
        }
        userAchievementMapper.delete(achievementQueryWrapper);
    }
}
