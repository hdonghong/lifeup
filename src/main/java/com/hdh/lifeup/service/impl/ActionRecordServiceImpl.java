package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.hdh.lifeup.dao.ActionRecordMapper;
import com.hdh.lifeup.model.domain.ActionRecordDO;
import com.hdh.lifeup.model.domain.ActionRecordGroupDO;
import com.hdh.lifeup.model.dto.ActionRecordDTO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserActionDTO;
import com.hdh.lifeup.model.dto.UserActionRecordDTO;
import com.hdh.lifeup.model.enums.ActionEnum;
import com.hdh.lifeup.service.ActionRecordService;
import com.hdh.lifeup.service.UserActionService;
import com.hdh.lifeup.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ActionRecordServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/08/08
 */
@Service
public class ActionRecordServiceImpl implements ActionRecordService {

    @Resource
    private ActionRecordMapper actionRecordMapper;
    @Resource
    private UserActionService userActionService;
    @Resource
    private UserInfoService userInfoService;

    @Override
    public void reportAction(ActionRecordDTO actionRecordDTO) {
        QueryWrapper<ActionRecordDO> countQuery = new QueryWrapper<ActionRecordDO>()
            .eq("user_id", actionRecordDTO.getUserId())
            .eq("action_id", actionRecordDTO.getActionId())
            .eq("Date(create_time)", LocalDate.now());
        Integer actionCount = actionRecordMapper.selectCount(countQuery);
        if (actionCount >= ActionEnum.getMaxLimit(actionRecordDTO.getActionId())) {
            return;
        }
        ActionRecordDO actionRecordDO = actionRecordDTO.toDO(ActionRecordDO.class);
        actionRecordMapper.insert(actionRecordDO);
    }

    @Override
    public PageDTO<UserActionRecordDTO> pageByUser(String language, Long userId, Integer pageNum, Integer pageSize) {
        // 获取用户行为记录
        QueryWrapper<ActionRecordDO> wrapper = new QueryWrapper<ActionRecordDO>().eq("user_id", userId)
            .orderByDesc("action_record_id");
        // 非VIP用户只能看过去14天
        Integer userType = userInfoService.getUserType(userId);
        if (Objects.equals(0, userType)) {
            wrapper = wrapper.gt("create_time", LocalDate.now().minus(14, ChronoUnit.DAYS));
        }
        IPage<ActionRecordDO> recordDOPage = actionRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        PageDTO<UserActionRecordDTO> recordDTOPage = PageDTO.createFreely(recordDOPage, UserActionRecordDTO.class);
        // 获取行为描述
        Map<Long, UserActionDTO> actionMap = userActionService.listAll(language).stream()
            .collect(Collectors.toMap(UserActionDTO::getActionId, userActionDTO -> userActionDTO));
        // 匹配
        recordDTOPage.getList().forEach(userActionRecordDTO -> {
            UserActionDTO userActionDTO = actionMap.get(userActionRecordDTO.getActionId());
            if (Objects.isNull(userActionDTO)) {
                return;
            }
            userActionRecordDTO.setActionDesc(userActionDTO.getActionDesc())
                .setActionScore(userActionDTO.getActionScore());
        });
        return recordDTOPage;
    }

    @Override
    public List<UserActionRecordDTO> listGroupByAction(String language, Long userId) {
        List<ActionRecordGroupDO> actionRecordGroupDOList = actionRecordMapper.listGroupByAction(userId);
        // 获取行为描述
        Map<Long, UserActionDTO> actionMap = userActionService.listAll(language).stream()
            .collect(Collectors.toMap(UserActionDTO::getActionId, userActionDTO -> userActionDTO));
        // 匹配
        return actionRecordGroupDOList.stream().map(actionRecordGroupDO -> {
            UserActionDTO userActionDTO = actionMap.get(actionRecordGroupDO.getActionId());
            if (Objects.isNull(userActionDTO)) {
                return null;
            }
            UserActionRecordDTO userActionRecordDTO = new UserActionRecordDTO();
            userActionRecordDTO.setActionId(actionRecordGroupDO.getActionId())
                .setUserId(userId)
                .setActionDesc(userActionDTO.getActionDesc())
                .setActionScore(actionRecordGroupDO.getTimes() * userActionDTO.getActionScore());
            return userActionRecordDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Integer getTotalScore(Long userId, LocalDateTime from, LocalDateTime to) {
        QueryWrapper<ActionRecordDO> query = new QueryWrapper<ActionRecordDO>()
            .eq("user_id", userId)
            .gt("create_time", from)
            .lt("create_time", to)
            .orderByAsc("action_record_id, action_id");
        List<ActionRecordDO> actionRecordDOList = actionRecordMapper.selectList(query);
        if (CollectionUtils.isEmpty(actionRecordDOList)) {
            return 0;
        }
        // 日期指针
        LocalDate datePointer = null;
        // 存储每个日期内每个action的总得分
        Map<Long, Integer> actionIdToSource = null;
        // 总得分
        int totalScore = 0;
        for (ActionRecordDO actionRecordDO : actionRecordDOList) {
            LocalDate newDate = actionRecordDO.getCreateTime().toLocalDate();
            if (!newDate.equals(datePointer)) {
                datePointer = newDate;
                actionIdToSource = Maps.newHashMap();
            }
            Long actionId = actionRecordDO.getActionId();
            Integer actionScore = actionIdToSource.getOrDefault(actionId, 0);
            ActionEnum actionEnum = ActionEnum.getEnum(actionId);
            if (actionEnum == null || actionScore >= actionEnum.getMaxLimit()) {
                continue;
            }
            actionIdToSource.put(actionId, actionScore + actionEnum.getScore());
            totalScore += actionEnum.getScore();
        }
        return totalScore;
    }


}
