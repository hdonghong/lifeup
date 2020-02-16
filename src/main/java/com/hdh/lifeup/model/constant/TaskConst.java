package com.hdh.lifeup.model.constant;

/**
 * TaskConst class<br/>
 * 任务的常量类
 * @author hdonghong
 * @since 2018/09/11
 */
public class TaskConst {

    /**
     * 团队成员角色
     */
    public static class TeamRole {

        public static final String OWNER = "OWNER";
        public static final String MEMBER = "MEMBER";
    }

    /** 动态的ICON */
    public static class ActivityIcon {

        public static final Integer IC_NEW = 0;
        public static final Integer IC_JOIN = 1;
        public static final Integer IC_SIGN = 2;
        public static final Integer IC_GIVE_UP = 3;
    }

    public static class TaskStatus {

        public static final Integer DOING = 0;
        public static final Integer COMPLETE = 2;
        public static final Integer GIVE_UP = 3;
    }

    /** 动态的显示范围 */
    public static class ActivityScope {

        /** 所有人都可以看到 */
        public static final Integer ALL = 3;
        /** 只有关注了我的人才能看到 */
        public static final Integer MYFOLLOWERS = 2;
        /** 只有自己能看到 */
        public static final Integer SECRET = 1;
    }

    /** 排序规则 */
    public static class RankRule {

        /** 团队活跃度优先 */
        public static final Integer TEAM_RANK_FIRST = 1;
    }

    /**
     * 团队创建来源
     */
    public static class CreateSource {

        /**
         * 国内，默认
         */
        public static final Integer CHINA = 1;

        /**
         * 海外
         */
        public static final Integer OVERSEAS = 2;
    }

    public static final String TIME_ZONE_GMT8 = "+8";
}