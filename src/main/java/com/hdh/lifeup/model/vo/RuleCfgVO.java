package com.hdh.lifeup.model.vo;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.RuleCfgDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * RuleCfgVO class<br/>
 *
 * @author hdonghong
 * @since 2020/10/08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class RuleCfgVO implements Serializable {

    private static final long serialVersionUID = 294098280862396754L;

    private Long ruleCfgId;

    private LocalDateTime updateTime;

    /**
     * 规则可额外携带的配置信息
     */
    private String cfgInfo;
}
