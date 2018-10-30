package com.hdh.lifeup.constant;

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
    }

    public static class TaskStatus {

        public static final Integer PREPARING = 0;
        public static final Integer DOING = 1;
        public static final Integer COMPLETE = 2;
        public static final Integer GIVE_UP = 3;
    }

}