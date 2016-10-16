package org.durcframework.core;

import javax.servlet.http.HttpSession;

public enum UserContext {
	INSTANCE;

	private static String S_KEY_USER = "S_KEY_USER";

	public static UserContext getInstance() {
		return INSTANCE;
	}

	/**
	 * 获取用户
	 * @return
	 */
	public <T extends IUser> T getUser() {
		HttpSession session = WebContext.getInstance().getSession();
		if(session != null){
			return (T)session.getAttribute(S_KEY_USER);
		}
		return null;
	}
	
	/**
	 * 获取用户
	 * @return
	 */
	public <T extends IUser> T getUser(HttpSession session) {
		return (T) session.getAttribute(S_KEY_USER);
	}

	/**
	 * 保存用户
	 * @param BackUser
	 */
	public <T extends IUser> void setUser(T t) {
		HttpSession session = WebContext.getInstance().getSession();
		if(session != null){
			session.setAttribute(S_KEY_USER, t);
		}
	}

	public boolean isAdmin() {
		return "admin".equals(getUser().getUsername());
	}
}
