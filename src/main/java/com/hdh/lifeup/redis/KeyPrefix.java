package com.hdh.lifeup.redis;

/**
 * @author hdonghong
 * @since 2018/09/24
 */
public interface KeyPrefix<T> {

	/**
	 * 缓存该key的存活时间，单位秒
	 * @return 存活时间，单位秒
	 */
	int expireSeconds();

	/**
	 * 该key的前缀
	 * @return key的前缀
	 */
	String getPrefix();

	/**
	 * 该key对应value的class类型
	 * @return 对应value的class类型
	 */
	Class<T> getValueClass();
}
