package com.hdh.lifeup.dao;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.model.domain.ActionRecordDO;
import com.hdh.lifeup.model.domain.ActionRecordGroupDO;
import com.hdh.lifeup.model.domain.LikeTeamTaskDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ActionRecordMapper interface<br/>
 *
 * @author hdonghong
 * @since 2020/08/03
 */
public interface ActionRecordMapper extends SuperMapper<ActionRecordDO> {

    @Select("SELECT action_id, count(*) times FROM action_record WHERE user_id = #{userId} GROUP BY action_id")
    List<ActionRecordGroupDO> listGroupByAction(@Param("userId") Long userId);
}
