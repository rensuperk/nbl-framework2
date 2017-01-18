package cn.bidlink.nbl.framework.auth.handler;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession; 
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.stereotype.Component; 
import cn.bidlink.nbl.framework.auth.utils.AuthContent;
import cn.bidlink.nbl.framework.auth.utils.AuthUtils;
import cn.bidlink.nbl.framework.core.context.TenantContext;
import cn.bidlink.nbl.user.UserContext;
import cn.bidlink.nbl.user.model.User;

/**
 *  设置 线程变量
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年10月10日 上午10:53:57 
 */ 
@SuppressWarnings("deprecation")
//@Component(value="threadLocalSetHandler")
public class ThreadLocalSetHandler extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpSession httpSession = ((HttpServletRequest)request).getSession();
		//获取当前用户
		User loginUser =  (User)httpSession.getAttribute( AuthContent.USER_LOGIN );
		//设置用户
		UserContext.setCurrentUser( loginUser );
		//0=主张号,1=子帐号
		if( loginUser.getTenantId() != null ){
			TenantContext.setTenantId( loginUser.getTenantId() );
		}else if( AuthUtils.isTenant( loginUser )){
			TenantContext.setTenantId( loginUser.getId() );
		}else{
			TenantContext.setTenantId( null );
			//throw new RuntimeException("login user not tenantid");
		}
		//0=正式数据,1=测试数据
		if( loginUser.getIsTest() == null ){
			TenantContext.setIsTest( false );
		}else if( loginUser.getIsTest() == 0 ){
			TenantContext.setIsTest( false );
		}else {
			TenantContext.setIsTest( true );	
		}
		chain.doFilter(request, response);
	}

}
