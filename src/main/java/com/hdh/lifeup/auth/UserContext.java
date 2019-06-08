package com.hdh.lifeup.auth;


import com.hdh.lifeup.model.dto.UserInfoDTO;

/**
 * 线程安全的用户引用
 * @author hdonghong
 */
public class UserContext {
	
	private static ThreadLocal<UserInfoDTO> userHolder = new ThreadLocal<>();
	
	public static void set(UserInfoDTO user) {
		userHolder.set(user);
	}
	
	public static UserInfoDTO get() {
		return userHolder.get();
	}

	public static void remove() {
		userHolder.remove();
	}

}
