package com.hdh.lifeup.redis;

import com.hdh.lifeup.util.TokenUtil;

/**
 * @author hdonghong
 * @since 2018/06/08
 */
public class LikeKey<T> extends BasePrefix<T> {

	private LikeKey(int expireSeconds, String prefix, Class<T> valueClass) {
		super(expireSeconds, prefix, valueClass);
	}

	public static final LikeKey<Long> ACTIVITY = new LikeKey<>(
			TokenUtil.EXPIRE_SECONDS, "activity", Long.class
	);

}
