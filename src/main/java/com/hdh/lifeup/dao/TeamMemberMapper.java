package com.hdh.lifeup.dao;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.model.domain.TeamMemberDO;
import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.vo.UserListVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TeamMemberMapper interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamMemberMapper extends SuperMapper<TeamMemberDO> {

    /**
     * 获取团队成员
     * @param teamId 团队id
     * @param pageDTO 条件
     * @return 成员列表
     */
    @Select("select m.user_id, m.team_id, m.create_time, u.nickname, u.user_head from team_member m, user_info u " +
            "where m.user_id = u.user_id " +
            "and m.team_id = #{teamId} " +
            "order by m.create_time desc limit #{page.currentPage}, #{page.size} ")
    List<UserListVO> getMembers(@Param("teamId") Long teamId, @Param("page") PageDTO pageDTO);

    @Select("select team_id from team_member where user_id = #{userId}")
    List<Long> getTeamIdsByUserId(@Param("userId") Long userId);

    /**
     * 查看用户过去30天参与的团队数
     * @param userId
     * @return
     */
    @Select("select count(1) from team_member where user_id = #{userId} " +
            "and DATE_SUB(CURDATE(), INTERVAL 30 DAY) < create_time")
    int countUserLast30DaysTeams(@Param("userId") Long userId);
}
