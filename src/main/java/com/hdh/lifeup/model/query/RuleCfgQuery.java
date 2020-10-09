package com.hdh.lifeup.model.query;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RuleCfgQuery class<br/>
 * @author hdonghong
 * @since 2020/10/08
 */
@Data
public class RuleCfgQuery implements Serializable {

    private static final long serialVersionUID = -7474426505020461130L;

    /**
     * 更新时间，如果匹配的规则与客户端传入的更新时间一致，说明客户端已持有该版本，不需要二次下发
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 【必填】规则组的key，具有相同组key的规则表示为同组规则
     */
    private String ruleGroupKey;

    /**
     * 版本号，包含
     * 对应`app_version`表的 new_version 字段
     */
    private Integer versionCode;

    /**
     * 支持的语言
     */
    private String language;

    /**
     * 发布渠道
     */
    private String publishChannel;
}
