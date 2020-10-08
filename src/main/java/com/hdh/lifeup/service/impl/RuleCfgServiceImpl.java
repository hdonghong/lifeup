package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.RuleCfgMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.domain.RuleCfgDO;
import com.hdh.lifeup.model.dto.RuleCfgDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.query.RuleCfgQuery;
import com.hdh.lifeup.service.RuleCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RuleCfgServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/10/08
 */
@Slf4j
@Service
public class RuleCfgServiceImpl implements RuleCfgService {

    @Autowired
    private RuleCfgMapper ruleCfgMapper;

    @Override
    public List<RuleCfgDTO> match(Long uid, RuleCfgQuery ruleCfgQuery) {
        // 要求指定规则组
        if (StringUtils.isEmpty(ruleCfgQuery.getRuleGroupKey())) {
            throw new GlobalException(CodeMsgEnum.PARAMETER_ERROR);
        }

        QueryWrapper<RuleCfgDO> ruleCfgQueryWrapper = new QueryWrapper<RuleCfgDO>()
            .eq("rule_group_key", ruleCfgQuery.getRuleGroupKey());
        List<RuleCfgDO> ruleCfgDOList = ruleCfgMapper.selectList(ruleCfgQueryWrapper);
        return ruleCfgDOList.stream()
            .map(RuleCfgDTO::fromDO)
            // 校验更新时间
            .filter(ruleCfgDTO -> !ruleCfgDTO.getUpdateTime().equals(ruleCfgQuery.getUpdateTime()))
            // 校验最小版本号
            .filter(ruleCfgDTO -> {
                // 当前规则不需要校验版本号
                if (ruleCfgDTO.getMinVersionCode() == null) {
                    return true;
                }
                // 当前规则需要校验版本号，但是查询入参却没有
                if (ruleCfgQuery.getVersionCode() == null) {
                    return false;
                }
                return ruleCfgQuery.getVersionCode() >= ruleCfgDTO.getMinVersionCode();
            })
            // 校验最大版本号
            .filter(ruleCfgDTO -> {
                // 当前规则不需要校验版本号
                if (ruleCfgDTO.getMaxVersionCode() == null) {
                    return true;
                }
                // 当前规则需要校验版本号，但是查询入参却没有
                if (ruleCfgQuery.getVersionCode() == null) {
                    return false;
                }
                return ruleCfgQuery.getVersionCode() <= ruleCfgDTO.getMaxVersionCode();
            })
            // 校验语言
            .filter(ruleCfgDTO -> {
                if (CollectionUtils.isEmpty(ruleCfgDTO.getLanguageList())) {
                    return true;
                }
                return ruleCfgDTO.getLanguageList().contains(ruleCfgQuery.getLanguage());
            })
            // 校验发布渠道
            .filter(ruleCfgDTO -> {
                if (CollectionUtils.isEmpty(ruleCfgDTO.getPublishChannel())) {
                    return true;
                }
                return ruleCfgDTO.getPublishChannel().contains(ruleCfgQuery.getPublishChannel());
            })
            // 校验是否灰度用户
            .filter(ruleCfgDTO -> {
                if (ruleCfgDTO.getUserGrayPercentage() == null) {
                    return true;
                }
                if (uid == null) {
                    return false;
                }
                return uid % 100 < ruleCfgDTO.getUserGrayPercentage();
            })
            .collect(Collectors.toList());
    }
}
