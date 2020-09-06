package com.hdh.lifeup.dao;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.model.domain.UserActionDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * UserActionMapper interface<br/>
 *
 * @author hdonghong
 * @since 2020/08/03
 */
public interface UserActionMapper extends SuperMapper<UserActionDO> {

    @Select("<script>" +
            "SELECT ua.action_id, ua.action_score, ua.score_limit, mt.text action_desc " +
            "FROM user_action ua, multilingual_text mt " +
            "WHERE ua.action_id = mt.related_id " +
            "AND mt.language = #{language} " +
            "</script>")
    List<UserActionDO> listByLanguage(@Param("language") String language);
}
