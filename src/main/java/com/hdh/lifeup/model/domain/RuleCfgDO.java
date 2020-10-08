package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * RuleCfgDO class<br/>
 * 规则配置
 * @author hdonghong
 * @since 2020/10/07
 */
@TableName("`rule_cfg`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RuleCfgDO extends BaseDO {

    private static final long serialVersionUID = 2029893434859068615L;

    @TableId
        private Long ruleCfgId;

        /** '创建时间' */
        private LocalDateTime createTime;

        private LocalDateTime updateTime;

        private Integer isDel;

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
        private String languageList;

        /**
         * 发布渠道，json数组格式
         */
        private String publishChannel;

        /**
         * 用户灰度的百分比，可空。0表示0%，即不支持任何用户。15表示支持15%的用户
         */
        private Integer userGrayPercentage;

        /**
         * 规则可额外携带的配置信息
         */
        private String cfgInfo;
}
