package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.UserRankMapper;
import com.hdh.lifeup.model.domain.UserRankDO;
import com.hdh.lifeup.model.dto.UserRankDTO;
import com.hdh.lifeup.service.AsyncTaskService;
import com.hdh.lifeup.service.TeamMemberService;
import com.hdh.lifeup.service.UserRankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.hdh.lifeup.model.constant.BizTypeConst.USER_INFO;
import static com.hdh.lifeup.model.enums.ActionEnum.GET_RANK;

/**
 * UserRankServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/05/18
 */
@Slf4j
@Service
public class UserRankServiceImpl implements UserRankService {

    @Resource
    private UserRankMapper userRankMapper;

    @Resource
    private TeamMemberService teamMemberService;
    @Resource
    private AsyncTaskService asyncTaskService;

    @Override
    public void updateRankValue(Long userId) {
        int rankValue = teamMemberService.getAttributeWeekly(userId);
        UserRankDO userRankDO = new UserRankDO();
        userRankDO.setUserId(userId);
        userRankDO.setRankValue((long) rankValue);
        QueryWrapper<UserRankDO> queryWrapper = new QueryWrapper<UserRankDO>()
                .eq("user_id", userId);
        Integer result = userRankMapper.update(userRankDO, queryWrapper);
        if (result == 0) {
            userRankMapper.insert(userRankDO);
        }
    }

    @Override
    public UserRankDTO getRankByUser(Long userId) {
        QueryWrapper<UserRankDO> queryWrapper = new QueryWrapper<UserRankDO>()
                .eq("user_id", userId);
        UserRankDO userRankDO = userRankMapper.selectOne(queryWrapper);
        if (userRankDO == null) {
            return UserRankDTO.bottomRank(userId);
        }
        asyncTaskService.reportAction(userId, GET_RANK, userId, USER_INFO);
        return UserRankDTO.from(userRankDO, UserRankDTO.class);
    }
}
