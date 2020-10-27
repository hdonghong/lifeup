package com.hdh.lifeup.model.enums;

import lombok.Getter;

/**
 * ActionEnum enum<br/>
 * 用户操作枚举（排行榜
 * @author hdonghong
 * @since 2020/07/06
 */
@Getter
public enum ActionEnum {

    /**
     * 社区部分
     */
    CREATE_TEAM(10001L, 3, 15),

    /**
     * 加入团队
     */
    JOIN_TEAM(10002L, 2, 10),

    /**
     * 点赞团队
     */
    LIKE_TEAM(10003L, 1, 10),

    /**
     * 签到
     */
    SIGN_IN(10004L, 2, 10),

    /**
     * 发表动态
     */
    ADD_ACTIVITY(10005L, 2, 10),

    /**
     * 浏览动态
     */
    BROWSE_ACTIVITY(10006L, 1, 5),

    /**
     * 点赞动态
     */
    LIKE_ACTIVITY(10007L, 1, 10),

    /**
     * 浏览团队
     */
    BROWSE_TEAM(10008L, 1, 5),

    /**
     * 查看团队详情
     */
    TEAM_DETAIL(1009L, 1, 5),

    /**
     * 查看排行榜
     */
    GET_RANK(10010L, 2, 10),

    /**
     * 查看用户
     */
    GET_USER(10011L, 1, 5),

    /**
     * 解锁成就
     */
    UNLOCK_ACHIEVEMENT(10012L, 3, 15),

    /**
     * 点赞商品
     */
    LIKE_GOODS(10013L, 1, 10),

    /**
     * 本地部分-------------------------------------------
     */
    PRIVATE_TASK(20001L, 3, 15),

    USER_PHOTO(20002L, 1, 5),

    TASK_READ_LATER(20003L, 1, 2),

    COMPLETE_PRIVATE_TASK(20004L, 2, 10),

    COMPLETE_SUB_TASK(20005L, 1, 10),

    COMPLETE_PRIVATE_ACTIVITY(20006L, 2, 10),

    CREATE_TASK_LIST(20007L, 3, 9),

    CREATE_GOODS_LIST(20008L, 3, 9),

    BROWSE_CALENDAR(20009L, 1, 3),

    STEPS_REWARD(20010L, 2, 2),

    PERSISTENCE_REWARD(20011L, 2, 2),

    CREATE_GOODS(20012L, 1, 5),

    BUY_GOODS(20013L, 2, 10),

    USE_GOODS(20014L, 1, 5),

    BROWSE_HISTORY_RECORDS(20015L, 1, 3),

    BROWSE_STATISTICS(20016L, 1, 3),

    SORT_TASK(20017L, 1, 5),

    CUSTOM_ACHIEVEMENT_CREATE_LIST(20018L, 2, 6),

    CUSTOM_ACHIEVEMENT_COMPLETE(20019L, 3, 9),

    CUSTOM_ACHIEVEMENT_CREATE(20020L, 3, 15),
    ;

    private Long actionId;
    private Integer score;
    private Integer maxLimit;

    ActionEnum(Long actionId, Integer score, Integer maxLimit) {
        this.actionId = actionId;
        this.score = score;
        this.maxLimit = maxLimit;
    }

    public static int getMaxLimit(Long actionId) {
        ActionEnum[] actionEnums = ActionEnum.values();
        for (ActionEnum actionEnum : actionEnums) {
            if (actionEnum.getActionId().equals(actionId)) {
                return actionEnum.getMaxLimit();
            }
        }
        return 0;
    }
}
