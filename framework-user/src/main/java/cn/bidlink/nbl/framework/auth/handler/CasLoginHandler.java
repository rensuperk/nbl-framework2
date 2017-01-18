package cn.bidlink.nbl.framework.auth.handler;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.bidlink.nbl.authority.service.AuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleAuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleOrganizationRemoteService;
import cn.bidlink.nbl.authority.service.MidUserRoleRemoteService;
import cn.bidlink.nbl.authority.service.ResourceRemoteService;
import cn.bidlink.nbl.framework.auth.service.AuthFacadeService;
import cn.bidlink.nbl.framework.auth.utils.AuthContent;
import cn.bidlink.nbl.framework.auth.utils.AuthUtils;
import cn.bidlink.nbl.user.core.enums.CommonEnum;
import cn.bidlink.nbl.user.model.Organization;
import cn.bidlink.nbl.user.model.User;
import cn.bidlink.nbl.user.service.OrganizationRemoteService;
import cn.bidlink.nbl.user.service.UserRemoteService;

/**
 * 登录处理 ：记录一些通用信息 ， 并记录单点登出所需要用的信息 
 * {@link cn.bind.framework.auth.authc.CasLogoutHandler}
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年9月18日 上午9:40:45 
 */
//@Component(value="casLoginHandler")
public class CasLoginHandler extends CasFilter{
	
	private static Logger logger = LoggerFactory.getLogger(CasLoginHandler.class); 
	
	@Autowired
	private OrganizationRemoteService organizationRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Value("${auth.index.page.url}")
	private String indexPageUrl;
	
//	@Autowired
//	private AuthFacadeService authFacadeService;
//	@Autowired
//	private AuthConfigManager authConfigManager;
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
	                                     ServletResponse response) throws Exception {
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		String loginName = (String)token.getPrincipal();
		try {
			//往Session中存储相关信息
			HttpSession session = httpServletRequest.getSession();
			User loginUser = userRemoteService.findUserByLoginName( loginName );
			if( loginUser == null ){
				httpServletResponse.getWriter().print("user not find");
				return false;
			}
			User tenantUser = null ;
			if( AuthUtils.isTenant( loginUser ) ){
				tenantUser = loginUser ;
			}else{
				tenantUser = getTenantUser( loginUser );
			}
			if( loginUser.getTenantId() == null && tenantUser != null){
				loginUser.setTenantId( tenantUser.getId() ); 
			}
			if( loginUser.getOrgId() != null ){
				Organization org = organizationRemoteService.findByPK(loginUser.getOrgId());
				if( org != null ){
					loginUser.setOrgCode( org.getAuthKey() );
				}
			}
			session.setAttribute( AuthContent.USER_LOGIN , loginUser );
			if( tenantUser != null ){
				session.setAttribute( AuthContent.USER_TENANT, tenantUser );
			}
			//session.setAttribute( AuthContent.TGT_KEY ,  request.getAttribute( AuthContent.TGT_KEY ) );
			session.setAttribute( AuthContent.TGT_KEY ,  token.getCredentials() );
		} catch (Exception e) {
			logger.error( "登录发生异常，账号="+loginName , e );
		} 
		return super.onLoginSuccess(token, subject, httpServletRequest, httpServletResponse);
	}
	
	@Override
	protected boolean onLoginFailure( AuthenticationToken token, AuthenticationException ae, ServletRequest request,
			ServletResponse response) { 
		return super.onLoginFailure(token, ae, request, response);
	}
	
	private String getIndex( User loginUser , HttpServletRequest httpServletRequest , HttpServletResponse  httpServletResponse){
//		String indexURl = getAuthConfigManager().getIndexPageURL();
		String domainName = (String)httpServletRequest.getAttribute("domainName");
		return indexPageUrl+"?from="+domainName;
	}
	
	/**
	 * 支持跳转到 上一个请求 所属的页面
	 */
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
    	final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    	final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
    	//设置跳转页
		HttpSession session = httpServletRequest.getSession();
		String preRequestRefer = (String)session.getAttribute( AuthContent.PRE_REQUEST_REFERER ) ;
		if( preRequestRefer != null && preRequestRefer.length() > 0 ){
			session.removeAttribute(  AuthContent.PRE_REQUEST_REFERER  );
		}else{
			User loginUser = (User)session.getAttribute( AuthContent.USER_LOGIN );
			preRequestRefer = getIndex( loginUser , httpServletRequest , httpServletResponse);
		}
		//设置页面不缓存
		httpServletResponse.setHeader("Cache-Control", "no-cache");  
		httpServletResponse.setHeader("Pragma", "no-cache");  
		httpServletResponse.setDateHeader("Expires", 0);  
		
        WebUtils.redirectToSavedRequest(httpServletRequest, httpServletResponse, preRequestRefer );
    }
	/**
	 * 获取 登录账号所属主账号
	 * @param companyId
	 * @return
	*/
	private User getTenantUser( User user ){
		 if( user == null ){
			return null ;
		 }
		 if( StringUtils.isNotBlank( user.getTenantId() ) ){
			return userRemoteService.findByPK( user.getTenantId() );
		 }
		 if( user.getOldCompanyId() != null ){
			 User tempUser = new User();
			 tempUser.setOldCompanyId( user.getOldCompanyId() ); 
			 tempUser.setStatus( CommonEnum.COMMON_STATUS_YES.getValue() );
			 tempUser.setIsSubject(  CommonEnum.IS_SUBJECT.getValue() );
			 List<User> users = userRemoteService.findByCondition( tempUser );
			 if( users != null && users.size() > 0 ){
				 return users.get( 0 );
			 }
		 }
		 return null ;
	} 
	 

//	public AuthFacadeService getAuthFacadeService() {
//		return authFacadeService;
//	}
//
//	public void setAuthFacadeService(AuthFacadeService authFacadeService) {
//		this.authFacadeService = authFacadeService;
//	}

//	public AuthConfigManager getAuthConfigManager() {
//		return authConfigManager;
//	}
//
//	public void setAuthConfigManager(AuthConfigManager authConfigManager) {
//		this.authConfigManager = authConfigManager;
//	}
}
