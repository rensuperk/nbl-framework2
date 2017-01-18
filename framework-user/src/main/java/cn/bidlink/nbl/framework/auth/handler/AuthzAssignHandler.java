package cn.bidlink.nbl.framework.auth.handler;

import cn.bidlink.nbl.authority.model.Authority;
import cn.bidlink.nbl.framework.auth.service.AuthFacadeService;
import cn.bidlink.nbl.framework.auth.validator.CustomCas20ServiceTicketValidator;
import cn.bidlink.nbl.user.service.UserRemoteService;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware; 
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 根据用户名，设置权限信息
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年8月13日 下午1:40:04 
 */
//@Component(value="authzAssignHandler")
public class AuthzAssignHandler extends CasRealm {

//	private ApplicationContext applicationContext; 
	
//	@Autowired
//	public AuthFacadeService authFacadeService;
//	@Autowired
//	public AuthConfigManager authConfigManager;
	@Autowired
	private UserRemoteService userRemoteService;
	
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<Authority> authorityList = userRemoteService.findAuthorityByLoginName( username );
        Set<String> authoritySet = new HashSet<String>();
        for (Authority authority : authorityList) {
        	authoritySet.add(  authority.getId() );
		}
        authorizationInfo.setStringPermissions(authoritySet);
        return authorizationInfo;
    }
	
//  @Override
//	public String getCasServerUrlPrefix() {
//		return getAuthConfigManager().getCasServiceURl();
//	}
//
//	@Override
//	public String getCasService() {
//		return getAuthConfigManager().getCasClientLoginURl();
//	}
	@Override
	public boolean isAuthorizationCachingEnabled() {
		return true;
	}
	@Override
	public boolean isAuthenticationCachingEnabled() {
		return true;
	}
	@Override
	public boolean isCachingEnabled() {
		return true;
	}
	@Override
	public String getAuthenticationCacheName() {
		return "authenticationCacheName";
	}
	@Override
	public String getAuthorizationCacheName() {
		return "authorizationCacheName";
	}
	
//	public AuthFacadeService getAuthFacadeService() {
//		return authFacadeService;
//	} 
//	public void setAuthFacadeService(AuthFacadeService authFacadeService) {
//		this.authFacadeService = authFacadeService;
//	}

//	public AuthConfigManager getAuthConfigManager() {
//		if(  authConfigManager == null ){
//			initAuthConfigManager();
//		}
//		return authConfigManager;
//	}
	
//	public void setAuthConfigManager(AuthConfigManager authConfigManager) {
//		this.authConfigManager = authConfigManager;
//	}

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
//	}
//	private synchronized void initAuthConfigManager(){
//		if( authConfigManager != null ){
//			return ;
//		}
//		authConfigManager = this.applicationContext.getBean( AuthConfigManager.class );
//	}
	protected TicketValidator createTicketValidator() {
        String urlPrefix = getCasServerUrlPrefix();
        return new CustomCas20ServiceTicketValidator( urlPrefix );
	}
}
