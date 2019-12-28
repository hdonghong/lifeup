package com.hdh.lifeup.dao;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.model.domain.TeamTaskDO;
import com.hdh.lifeup.model.dto.PageDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * TeamTaskMapper interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamTaskMapper extends SuperMapper<TeamTaskDO> {

    /**
     * 获取用户加入的团队列表
     * @param userId
     * @param pageDTO
     * @param isOwner
     * @return
     */
    @Select("<script>" +
            "select t.* from team_task t, team_member m where t.team_id = m.team_id " +
            "and m.user_id = #{userId} " +
            "and t.team_status = #{teamStatus} " +
            "and t.is_del = 0 " +
            "<if test='isOwner == true'> AND t.user_id = #{userId} </if>" +
            "order by m.create_time desc limit #{page.currentPage}, #{page.size} " +
            "</script>")
    List<TeamTaskDO> getUserTeams(@Param("userId") Long userId, @Param("page") PageDTO pageDTO,
                                  @Param("teamStatus") Integer teamStatus, @Param("isOwner") Boolean isOwner);

    @Select("select count(1) from team_task t, team_member m where t.team_id = m.team_id " +
            "and m.user_id = #{userId} " +
            "and t.team_status = #{teamStatus} " +
            "and t.is_del = 0 ")
    int countUserTeamsWithStatus(@Param("userId") Long userId, @Param("teamStatus") Integer teamStatus);

    /**
     * 增加团队活跃度排序值，e 小于0时则减少
     * @param teamId
     * @param e
     * @return
     */
    @Update("update team_task set team_rank = team_rank + #{e} where team_id = #{teamId} limit 1")
    int incrTeamRank(@Param("teamId") Long teamId, @Param("e") int e);
}
