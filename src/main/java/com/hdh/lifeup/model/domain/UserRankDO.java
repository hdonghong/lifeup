package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserRankDO class<br/>
 * 排行榜
 * @author hdonghong
 * @since 2020/05/18
 */
@TableName("`user_rank`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserRankDO extends BaseDO {

    private static final long serialVersionUID = 896134336847401089L;

    @TableId
    private Long rankId;

    private Long userId;

    private Long rankValue;

    @TableLogic
    private Integer isDel;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
