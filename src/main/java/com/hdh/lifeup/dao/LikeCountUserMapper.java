package com.hdh.lifeup.dao;

import com.hdh.lifeup.base.SuperMapper;
import com.hdh.lifeup.model.domain.AppVersionDO;
import com.hdh.lifeup.model.domain.LikeCountUserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * LikeCountUserMapper interface<br/>
 *
 * @author hdonghong
 * @since 2019/06/08
 */
public interface LikeCountUserMapper extends SuperMapper<LikeCountUserDO> {

    @Update("update like_count_user set like_count = like_count + #{e} where user_id = #{userId} limit 1")
    Integer incr(@Param("userId") Long userId, @Param("e") int e);
}
