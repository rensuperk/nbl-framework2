package cn.bidlink.nbl.framework.auth.handler;

import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.bidlink.framework.core.utils.response.ResponseUtils;
import cn.bidlink.nbl.framework.auth.utils.AuthContent;

/**
 * 验证用户是否登录，未登录时，重定向至登录页
 * 
 * 检查是否认证（登录） 并同时解决 异步和同步请求，前台处理的异同
 * 异步：当未登录时，弹出对话框
 * 同步：当未登录时，返回 cas登录页
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年9月18日 上午11:30:42 
 */
//@Component(value="authcCheckHandler")
public class AuthcCheckHandler extends UserFilter {
	
	public final Logger logger = LoggerFactory.getLogger(getClass());
	
//	@Autowired
//	private AuthConfigManager authConfigManager; 
	
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		String requestType = httpServletRequest.getHeader("X-Requested-With");
		String acceptType = httpServletRequest.getHeader("accept");
		String referer = httpServletRequest.getHeader("Referer");
		boolean isAjaxRequest = StringUtils.isNotBlank( requestType ) || acceptType.contains("application/json");
		StringBuffer cookieValue = new StringBuffer();
		if( referer != null && isAjaxRequest == true){
			cookieValue.append( referer );
		}else{
			cookieValue.append( httpServletRequest.getRequestURL() );
			if ( httpServletRequest.getQueryString() != null) { 
				cookieValue.append('?'); 
				cookieValue.append( httpServletRequest.getQueryString() ); 
			} 
		}
		SimpleCookie simpleCookie = new SimpleCookie( AuthContent.PRE_REQUEST_REFERER );
		simpleCookie.setValue( URLEncoder.encode( cookieValue.toString() ,"UTF-8" ) );
		simpleCookie.saveTo( httpServletRequest , httpServletResponse );
		//HTTP_REFERER 可以添加此头
		if( isAjaxRequest ){
			String loginUrl = getLoginUrl();
			ResponseUtils.responseJson(httpServletResponse, "{\"resStatus\":420,\"resMsg\":\"请登录!\",\"params\":{\"obj\":{\"referer\":\""+loginUrl+"\"}}}");
		}else{
			saveRequestAndRedirectToLogin(request, response);
		} 
        return false;
    }
	
//	@Override
//	public String getLoginUrl() {
//		@SuppressWarnings("deprecation")
//		String loginUrl = getAuthConfigManager().getCasServiceURl()+"/login"
//			+"?service="+getAuthConfigManager().getCasClientLoginURl()
//			+"&realService="+getAuthConfigManager().getCasClientLogoutURl()
//			+"&loginUrl="+ URLEncoder.encode( getAuthConfigManager().getCommonLoginPageURL() ) ;
//		return loginUrl;
//		/*WebDelegatingSubject subject = (WebDelegatingSubject)SecurityUtils.getSubject();
//		HttpServletRequest request =  (HttpServletRequest)subject.getServletRequest() ;
//		String referer = PathUtils.getDomainName( request );
//		String afdoa = getAuthConfigManager().getCommonLoginPageURL()
//				+"?service="+getAuthConfigManager().getCasClientLoginURl()
//				+"&referer="+referer
//				+"&realService="+getAuthConfigManager().getCasClientLogoutURl();
//		
//		@SuppressWarnings("deprecation")
//		String loginUrl = getAuthConfigManager().getCasServiceURl()+"/login"
//			+"?service="+getAuthConfigManager().getCasClientLoginURl()
//			+"&realService="+getAuthConfigManager().getCasClientLogoutURl()
//			+"&loginUrl="+ URLEncoder.encode( afdoa )
//			+"&referer="+ referer;
//		 
//		return loginUrl;*/
//	}
	
//	public AuthConfigManager getAuthConfigManager() {
//		return authConfigManager;
//	}
//	public void setAuthConfigManager(AuthConfigManager authConfigManager) {
//		this.authConfigManager = authConfigManager;
//	}
}
