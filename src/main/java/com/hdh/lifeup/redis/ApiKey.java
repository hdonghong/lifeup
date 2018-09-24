package com.hdh.lifeup.redis;

/**
 * @author hdonghong
 * @since 2018/09/24
 */
public class ApiKey<T> extends BasePrefix<T>{

	private ApiKey(int expireSeconds, String prefix, Class<T> valueClass) {
		super(expireSeconds, prefix, valueClass);
	}

	public static ApiKey<Long> withExpire(int expireSeconds) {
		return new ApiKey<>(expireSeconds, "api", Long.TYPE);
	}

}
