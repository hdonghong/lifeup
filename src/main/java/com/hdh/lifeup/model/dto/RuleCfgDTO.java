package com.hdh.lifeup.model.dto;

import com.google.common.base.Preconditions;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.RuleCfgDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RuleCfgDTO class<br/>
 *
 * @author hdonghong
 * @since 2020/10/08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RuleCfgDTO extends BaseDTO<RuleCfgDO> {

    private static final long serialVersionUID = 294098280862396754L;

    private Long ruleCfgId;

    private LocalDateTime updateTime;

    /**
     * 规则组的key，具有相同组key的规则表示为同组规则
     */
    private String ruleGroupKey;

    /**
     * 开始的版本号，有包含
     * 对应`app_version`表的 new_version 字段
     */
    private Integer minVersionCode;

    /**
     * 结束的版本号，有包含
     * 对应`app_version`表的 new_version 字段
     */
    private Integer maxVersionCode;

    /**
     * 支持的语言列表，json数组格式
     */
    private List<String> languageList;

    /**
     * 发布渠道
     */
    private List<String> publishChannel;

    /**
     * 用户灰度的百分比，可空。0表示0%，即不支持任何用户。15表示支持15%的用户
     */
    private Integer userGrayPercentage;

    /**
     * 规则可额外携带的配置信息
     */
    private String cfgInfo;

    public static RuleCfgDTO fromDO(RuleCfgDO ruleCfgDO) {
        RuleCfgDTO ruleCfgDTO = new RuleCfgDTO();
        BeanUtils.copyProperties(ruleCfgDO, ruleCfgDTO);
        if (!StringUtils.isEmpty(ruleCfgDO.getLanguageList())) {
            List<String> languageList = JsonUtil.jsonToList(ruleCfgDO.getLanguageList(), String.class);
            ruleCfgDTO.setLanguageList(languageList);
        }
        if (!StringUtils.isEmpty(ruleCfgDO.getPublishChannel())) {
            List<String> publishChannel = JsonUtil.jsonToList(ruleCfgDO.getPublishChannel(), String.class);
            ruleCfgDTO.setPublishChannel(publishChannel);
        }
        return ruleCfgDTO;
    }
}
