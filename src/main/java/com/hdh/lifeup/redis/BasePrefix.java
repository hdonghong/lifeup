package com.hdh.lifeup.redis;

/**
 * @author hdonghong
 * @since 2018/09/24
 */
public abstract class BasePrefix<T> implements KeyPrefix<T> {
	
	private int expireSeconds;
	
	private String prefix;

	private Class<T> valueClass;


	/**
	 * -1代表永不过期
	 * @param prefix 前缀
	 */
	public BasePrefix(String prefix, Class<T> valueClass) {
		this(-1, prefix, valueClass);
	}
	
	public BasePrefix(int expireSeconds, String prefix, Class<T> valueClass) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
		this.valueClass = valueClass;
	}

	@Override
	public int expireSeconds() {//默认0代表永不过期
		return expireSeconds;
	}

	@Override
	public String getPrefix() {
		String className = getClass().getSimpleName();
		return className + ":" + prefix + ":";
	}

	@Override
	public Class<T> getValueClass() {
		return valueClass;
	}
}
