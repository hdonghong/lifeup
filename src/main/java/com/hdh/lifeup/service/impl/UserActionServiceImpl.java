package com.hdh.lifeup.service.impl;

import com.google.common.collect.Lists;
import com.hdh.lifeup.dao.UserActionMapper;
import com.hdh.lifeup.model.domain.UserActionDO;
import com.hdh.lifeup.model.dto.UserActionDTO;
import com.hdh.lifeup.model.enums.LanguageEnum;
import com.hdh.lifeup.service.UserActionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserActionServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/08/09
 */
@Service
public class UserActionServiceImpl implements UserActionService {

    @Resource
    private UserActionMapper userActionMapper;


    @Override
    public List<UserActionDTO> listAll(String language) {
        if (LanguageEnum.SIMPLIFIED_CHINESE.getValue().equals(language)) {
            List<UserActionDO> userActionDOList = userActionMapper.selectList(null);
            return transUserActionDO2DTO(userActionDOList);
        }
        List<UserActionDO> userActionDOList = userActionMapper.listByLanguage(LanguageEnum.toValue(language));
        // 兜底，默认英文
        if (CollectionUtils.isEmpty(userActionDOList)) {
            userActionDOList = userActionMapper.listByLanguage(LanguageEnum.ENGLISH.getValue());
        }
        return transUserActionDO2DTO(userActionDOList);
    }

    private List<UserActionDTO> transUserActionDO2DTO(List<UserActionDO> userActionDOList) {
        if (CollectionUtils.isEmpty(userActionDOList)) {
            return Lists.newArrayList();
        }
        return userActionDOList.stream()
            .map(userActionDO -> UserActionDTO.from(userActionDO, UserActionDTO.class))
            .collect(Collectors.toList());
    }
}
