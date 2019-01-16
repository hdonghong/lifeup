package com.hdh.lifeup.mapper;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.domain.TeamTaskDO;
import com.hdh.lifeup.dto.PageDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * @return
     */
    @Select("select t.* from team_task t, team_member m where t.team_id = m.team_id " +
            "and m.user_id = #{userId} " +
            "and t.is_del = 0 " +
            "order by m.create_time desc limit #{page.currentPage}, #{page.size} ")
    List<TeamTaskDO> getUserTeams(@Param("userId") Long userId, @Param("page") PageDTO pageDTO);
}
