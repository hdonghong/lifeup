package com.hdh.lifeup.model.enums;

import lombok.Getter;

/**
 * ActionEnum enum<br/>
 * 用户操作枚举
 * @author hdonghong
 * @since 2020/07/06
 */
@Getter
public enum ActionEnum {

    /**
     * 社区部分
     */
    CREATE_TEAM(10001, 3, 15),

    JOIN_TEAM(10002, 2, 10),

    LIKE_TEAM(10003, 1, 10),

    SIGN_IN(10004, 2, 10),

    SHARE_ACTIVITY(10006, 2, 10),

    BROWSE_ACTIVITY(10007, 1, 5),

    LIKE_ACTIVITY(10008, 1, 10),

    BROWSE_TEAM(10009, 1, 5),

    TEAM_DETAIL(10010, 1, 5),

    GET_RANK(10011, 2, 10),

    GET_USER(10012, 1, 5),

    UNLOCK_ACHIEVEMENT(10013, 3, 15),

    /**
     * 本地部分
     */
    PRIVATE_TASK(20001, 3, 15),

    USER_PHOTO(20002, 1, 5),

    TASK_READ_LATER(20003, 1, 2),

    COMPLETE_PRIVATE_TASK(20004, 2, 10),

    COMPLETE_SUB_TASK(20005, 1, 10),

    COMPLETE_PRIVATE_ACTIVITY(20006, 2, 10),

    CREATE_TASK_LIST(20007, 3, 9),

    CREATE_GOODS_LIST(20008, 3, 9),

    BROWSE_CALENDAR(20009, 1, 3),

    STEPS_REWARD(20010, 2, 2),

    PERSISTENCE_REWARD(20011, 2, 2),

    CREATE_GOODS(20012, 1, 5),

    BUY_GOODS(20013, 2, 10),

    USE_GOODS(20014, 1, 5),

    BROWSE_HISTORY_RECORDS(20015, 1, 3),

    BROWSE_STATISTICS(20016, 1, 3),

    SORT_TASK(20017, 1, 5),
    ;

    private Integer actionId;
    private Integer score;
    private Integer maxLimit;

    ActionEnum(Integer actionId, Integer score, Integer maxLimit) {
        this.actionId = actionId;
        this.score = score;
        this.maxLimit = maxLimit;
    }
}
