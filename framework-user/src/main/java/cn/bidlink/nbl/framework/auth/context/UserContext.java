package cn.bidlink.nbl.framework.auth.context;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.WebSubject;
import cn.bidlink.nbl.framework.auth.utils.AuthContent;
import cn.bidlink.nbl.user.model.User;

/**
 * @author 刘佳明
 * */
public class UserContext {
	/**
	 * 获取当前登陆用户
	 * @see 获取Session中的用户信息
	 * @deprecated 使用{@link cn.bidlink.nbl.user.UserContext}
	 * */
	@Deprecated
	public static final User getLoginUser(){
		//移除 对spring mvc 的依赖
		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		ServletRequest servletRequest = ((WebSubject)SecurityUtils.getSubject()).getServletRequest();
		HttpServletRequest request = (HttpServletRequest) servletRequest ;
		return (User) request.getSession().getAttribute(AuthContent.USER_LOGIN);
	}
}