package com.hdh.lifeup.redis;

import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;

/**
 * MemberRecordKey class<br/>
 *
 * @author hdonghong
 * @since 2019/06/08
 */
public class MemberRecordKey<T> extends BasePrefix<T> {

    protected MemberRecordKey(int expireSeconds, String prefix, Class<T> valueClass) {
        super(expireSeconds, prefix, valueClass);
    }

    public static final MemberRecordKey<TeamMemberRecordDTO> ID = new MemberRecordKey<>(
            60 * 5, "id", TeamMemberRecordDTO.class
    );
}
