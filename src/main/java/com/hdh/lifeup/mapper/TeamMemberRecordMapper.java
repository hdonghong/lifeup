package com.hdh.lifeup.mapper;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.domain.TeamMemberRecordDO;
import com.hdh.lifeup.dto.PageDTO;
import com.hdh.lifeup.dto.RecordDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * TeamMemberRecordMapper interface<br/>
 *
 * @author hdonghong
 * @since 2018/09/02
 */
public interface TeamMemberRecordMapper extends SuperMapper<TeamMemberRecordDO> {

    /**
     * 获取某个团队成员动态
     * @param teamId 团队
     * @param pageDTO 条件
     * @return 列表
     */
    @Select("select r.*, u.nickname, u.user_head from team_member_record r, user_info u " +
            "where r.user_id = u.user_id " +
            "and r.team_id = #{teamId} " +
            "order by r.create_time desc limit #{page.currentPage}, #{page.size} ")
    List<RecordDTO> getMemberRecords(@Param("teamId") Long teamId, @Param("page") PageDTO pageDTO);

    @Select("<script>" +
            "select r.*, u.nickname, u.user_head from team_member_record r, user_info u " +
            "where r.user_id = u.user_id " +
            "and u.user_id in " +
                "<foreach item='id' index='index' collection='userIds' open='(' separator=',' close=')'>" +
                "${id}" +
                "</foreach>" +
            "order by r.create_time desc limit #{page.currentPage}, #{page.size} " +
            "</script>")
    List<RecordDTO> getRecordsByUserIds(@Param("userIds") Collection<Long> userIds, @Param("page") PageDTO pageDTO);


    /** 获取所有人可见的动态数据 */
    @Select("select r.*, u.nickname, u.user_head from team_member_record r, user_info u " +
            "where r.user_id = u.user_id " +
            "and r.activity_icon <> 1 " +
            "and r.activity_scope = 3 " +
            "order by r.create_time desc limit #{page.currentPage}, #{page.size} ")
    List<RecordDTO> getRecords(@Param("page") PageDTO pageDTO);
}
