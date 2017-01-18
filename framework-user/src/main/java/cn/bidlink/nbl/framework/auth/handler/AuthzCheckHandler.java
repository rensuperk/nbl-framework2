package cn.bidlink.nbl.framework.auth.handler;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.bidlink.framework.core.utils.response.ResponseUtils;
import cn.bidlink.nbl.authority.model.Authority;
import cn.bidlink.nbl.authority.service.ResourceRemoteService;
import cn.bidlink.nbl.framework.auth.utils.PathUtils;
import cn.bidlink.nbl.framework.auth.service.AuthFacadeService; 

/**
 * 对 用户的请求 进行 权限检查
 * 后期根据情况 对数据库的查询结果做缓存 
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年9月18日 上午11:18:10 
 */
//@Component(value="authzCheckHandler")
public class AuthzCheckHandler extends AccessControlFilter {
	
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	
	@Value("${auth.product.upgrade.url}")
	private String productUpgradeUrl;
	
//	@Autowired
//	public AuthFacadeService authFacadeService;
//	@Autowired
//	private AuthConfigManager authConfigManager;
	
	private final static String NO_AUTH_URL = AuthzCheckHandler.class.getName() +"_noAuthUrl"; 
	/**
	 * 验证规则：
	 * 假如资源 在数据库中不存在，则表示该资源不在权限保护范围内
	 * 假如资源 在数据库中存在，并且包含在某个权限内，如果用户的权限集合
	 * 不包含该权限则拒绝访问
	 * @return true 允许访问 ，false 拒绝访问
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest)request; 
		Subject subject = SecurityUtils.getSubject();  
		String servletPath = getServletPath( httpRequest );
		//根据请求URl 获取 权限
		List<Authority> authorityList = resourceRemoteService.findAuthorityByResource(servletPath);
		//验证权限
		boolean isAuth = true ; 
		String noAuthUrl = "";
		for (Authority authority : authorityList) {
			 isAuth = subject.isPermitted( authority.getId() );
			 if( isAuth ){
				 break ;
			 }
			 noAuthUrl = authority.getNoAuthUrl();
		}
		if( isAuth == false ){
			if( StringUtils.isBlank( noAuthUrl ) || "/".equals( noAuthUrl ) ){
				noAuthUrl = productUpgradeUrl;//getAuthConfigManager().getProductUpgradeUrl();
			}
			httpRequest.setAttribute( AuthzCheckHandler.NO_AUTH_URL , noAuthUrl );
		}
		return isAuth;
	} 
	/**
	 * 目前仅 支持 xxx/get/${id} 路径的rest风格
	 * 数据库录入此rest 路径时 需要在get后面添加上/符号，表明 不是最后一级路径
	 */
	protected String getServletPath( HttpServletRequest httpServletRequest ){
		return PathUtils.getServletPath(httpServletRequest);
	}
	/**
	 * 当没有权限时，针对不同情况对前台进行不同的响应
	 * 1、ajax 请求：响应json信息
	 * 2、同步请求：重定向页面
	 * @return false 表示自己已经处理过 ，不需要再执行后面的过滤器
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		final HttpServletRequest httpRequest = (HttpServletRequest)request;
		final HttpServletResponse httpResponse = (HttpServletResponse)response;
		//响应无权限页面 或状态码
		String requestType = httpRequest.getHeader("X-Requested-With");
		String noAuthUrl = (String)httpRequest.getAttribute( AuthzCheckHandler.NO_AUTH_URL );
		if( StringUtils.isBlank( requestType ) ){
			if( noAuthUrl != null && noAuthUrl.length() > 0 ){
				WebUtils.issueRedirect(request, response, noAuthUrl);	
			}
		}else{  
		   ResponseUtils.responseJson(httpResponse, "{\"resStatus\":420,\"resMsg\":\"没有权限操作,请联系管理人员!\",\"params\":{\"obj\":{\"referer\":\""+noAuthUrl+"\"}}}");
		}
		return false;
	}
//	public AuthFacadeService getAuthFacadeService() {
//		return authFacadeService;
//	}
//	public void setAuthFacadeService(AuthFacadeService authFacadeService) {
//		this.authFacadeService = authFacadeService;
//	}
//	public AuthConfigManager getAuthConfigManager() {
//		return authConfigManager;
//	}
//	public void setAuthConfigManager(AuthConfigManager authConfigManager) {
//		this.authConfigManager = authConfigManager;
//	}
}
