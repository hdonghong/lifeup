package com.hdh.lifeup.mapper;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.domain.TeamMemberDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.vo.MembersVO;
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
    List<MembersVO> getMembers(@Param("teamId") Long teamId, @Param("page") PageDTO pageDTO);
}
