package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.RuleCfgDTO;
import com.hdh.lifeup.model.query.RuleCfgQuery;
import com.hdh.lifeup.model.vo.RuleCfgVO;

import java.util.List;

/**
 * RuleCfgService interface<br/>
 * 规则配置
 * @author hdonghong
 * @since 2020/10/08
 */
public interface RuleCfgService {

    /**
     * 规则匹配
     * @param uid 用户id
     * @param ruleCfgQuery 规则配置查询
     * @return
     */
    List<RuleCfgVO> match(Long uid, RuleCfgQuery ruleCfgQuery);
}
