package com.hdh.lifeup.redis;

import com.hdh.lifeup.dto.UserInfoDTO;
import com.hdh.lifeup.util.TokenUtil;
import com.hdh.lifeup.vo.UserListVO;

/**
 * @author hdonghong
 * @since 2018/09/24
 */
public class UserKey<T> extends BasePrefix<T> {

	private UserKey(int expireSeconds, String prefix, Class<T> valueClass) {
		super(expireSeconds, prefix, valueClass);
	}

	public static final UserKey<UserInfoDTO> TOKEN = new UserKey<>(
			TokenUtil.EXPIRE_SECONDS, "token", UserInfoDTO.class
	);

	public static final UserKey<Long> FOLLOWING = new UserKey<>(
			TokenUtil.EXPIRE_SECONDS, "following", Long.class
	);

	public static final UserKey<Long> FOLLOWER = new UserKey<>(
			TokenUtil.EXPIRE_SECONDS, "follower", Long.class
	);

	public static final UserKey<Long> LIKE_ACTIVITY = new UserKey<>(
			TokenUtil.EXPIRE_SECONDS, "like_activity", Long.class
	);

}
