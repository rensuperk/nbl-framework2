package cn.bidlink.nbl.framework.auth.access;

import javax.servlet.http.HttpServletRequest;

public interface AccessDecisionService {
	/**
	 * 验证是否白名单
	 * true 是， false:否。
	 */
	public boolean isAllow( HttpServletRequest httpServletRequest);
}
