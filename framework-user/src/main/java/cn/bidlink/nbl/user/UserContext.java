package cn.bidlink.nbl.user;

import cn.bidlink.nbl.user.model.User;

/**
 * @author 刘佳明
 * */
public final class UserContext {
	
	private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();
	
	public static final void setCurrentUser(User user){
		CURRENT_USER.set(user);
	}
	
	public static final User getCurrentUser(){
		return CURRENT_USER.get();
	}
	
	public static final User getSystemUser(){
		return new User();
	}
}