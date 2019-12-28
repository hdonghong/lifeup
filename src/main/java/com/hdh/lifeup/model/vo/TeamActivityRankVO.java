package com.hdh.lifeup.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * TeamActivityRankVO class<br/>
 * 团队活跃度排序值
 *
 * @author hdonghong
 * @since 2019/12/09
 */
@Data
@Accessors(chain = true)
public class TeamActivityRankVO {

    /** 动态数 */
    private Integer activityCount;

    /** 团队成员数 */
    private Integer teamMemberCount;

    /** 创建者总动态数 */
    private Integer ownerActivityCount;

    /** 创建者参与团队数 */
    private Integer ownerTeamCount;

    public Integer getActivityCount() {
        return activityCount == null ? 0 : activityCount;
    }

    public Integer getTeamMemberCount() {
        return teamMemberCount == null ? 0 : teamMemberCount;
    }

    public Integer getOwnerActivityCount() {
        return ownerActivityCount == null ? 0 : ownerActivityCount;
    }

    public Integer getOwnerTeamCount() {
        return ownerTeamCount == null ? 0 : ownerTeamCount;
    }

    public int getTeamRank() {
        return getActivityCount() * WEIGHT_FOUR +
                getTeamMemberCount() * WEIGHT_DOUBLE +
                getOwnerTeamCount() * WEIGHT_ONE +
                getOwnerActivityCount() + WEIGHT_ONE;
    }

    private static final int WEIGHT_ONE = 1;

    private static final int WEIGHT_DOUBLE = 2;

    private static final int WEIGHT_FOUR = 4;
}
