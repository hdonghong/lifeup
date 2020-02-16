package com.hdh.lifeup.auth;


/**
 * 线程安全的用户引用
 * @author hdonghong
 */
public class TimeZoneContext {

	private static ThreadLocal<String> timeZoneHolder = new ThreadLocal<>();

	public static void set(String localTimeZone) {
		timeZoneHolder.set(localTimeZone);
	}

	public static String get() {
		return timeZoneHolder.get();
	}

	public static void remove() {
		timeZoneHolder.remove();
	}
}
