package com.hdh.lifeup.mapper;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.domain.TeamMemberRecordDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.RecordDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TeamMemberRecordMapper interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamMemberRecordMapper extends SuperMapper<TeamMemberRecordDO> {

    /**
     * 获取团队动态
     * @param teamId 团队
     * @param pageDTO 条件
     * @return 列表
     */
    @Select("select r.*, u.nickname, u.user_head from team_member_record r, user_info u " +
            "where r.user_id = u.user_id " +
            "and r.team_id = #{teamId} " +
            "order by r.create_time desc limit #{page.currentPage}, #{page.size} ")
    List<RecordDTO> getMembersRecords(@Param("teamId") Long teamId, @Param("page") PageDTO pageDTO);
}
