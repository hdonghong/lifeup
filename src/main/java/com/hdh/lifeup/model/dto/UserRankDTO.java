package com.hdh.lifeup.model.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.UserRankDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserRankDTO class<br/>
 * 排行榜
 * @author hdonghong
 * @since 2020/05/17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserRankDTO extends BaseDTO<UserRankDO> {

    private static final long serialVersionUID = 2922768667720679703L;

    private Long rankId;

    private Long userId;

    private Long rankValue;

    /** '创建时间' */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static UserRankDTO bottomRank(Long userId) {
        UserRankDTO userRankDTO = new UserRankDTO();
        userRankDTO.setRankValue(0L);
        userRankDTO.setUserId(userId);
        return userRankDTO;
    }
}
