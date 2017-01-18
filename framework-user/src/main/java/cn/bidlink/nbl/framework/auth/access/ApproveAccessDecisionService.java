package cn.bidlink.nbl.framework.auth.access;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware; 
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.bidlink.framework.core.utils.crypto.MD5;
import cn.bidlink.nbl.framework.auth.service.AuthFacadeService;
import cn.bidlink.nbl.framework.auth.service.DubboAuthFacadeService; 
import cn.bidlink.nbl.framework.auth.utils.AuthUtils;
import cn.bidlink.nbl.framework.core.context.TenantContext;
import cn.bidlink.nbl.user.UserContext;
import cn.bidlink.nbl.user.model.User;
import cn.bidlink.nbl.user.service.UserRemoteService;

@SuppressWarnings("deprecation")
@Component
public class ApproveAccessDecisionService implements AccessDecisionService {
	
//	private ApplicationContext applicationContext;
//	private AuthFacadeService authFacadeService;
	
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	public boolean isAllow(HttpServletRequest httpServletRequest) {
		if( httpServletRequest.getParameter("approvalResultDto") != null && httpServletRequest.getParameter("key") != null ){
			JSONObject approvalResultDto = JSONObject.parseObject( httpServletRequest.getParameter("approvalResultDto") );
			String key = httpServletRequest.getParameter("key") ;
			String templateId = approvalResultDto.getString("templateId");
			String businessId = approvalResultDto.getString("businessId");
			
			String accessKey = MD5.encrypt(templateId + "!@#$%^&*" + businessId) ;
			if(! StringUtils.equals( key , accessKey )){
				throw new RuntimeException("访问码不正常");
			}
			//获取审批发起人
			String applyUserId = approvalResultDto.getString("applyUserId");
			if( StringUtils.isNotBlank( applyUserId )){
				User currentUser = userRemoteService.findByPK( applyUserId );
				if( currentUser != null ){
					UserContext.setCurrentUser( currentUser );
					//0=主张号,1=子帐号
					if( currentUser.getTenantId() != null ){
						TenantContext.setTenantId( currentUser.getTenantId() );
					}else if( AuthUtils.isTenant( currentUser )){
						TenantContext.setTenantId( currentUser.getId() );
					}else{
						TenantContext.setTenantId( null );
					}
					//0=正式数据,1=测试数据
					if( currentUser.getIsTest() == null ){
						TenantContext.setIsTest( false );
					}else if( currentUser.getIsTest() == 0 ){
						TenantContext.setIsTest( false );
					}else {
						TenantContext.setIsTest( true );	
					}
				}
			}
			return true ;
		}
		return false;
	}

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		this.applicationContext = applicationContext;
//		
//	}
//	private AuthFacadeService getAuthFacadeService(){
//		if( authFacadeService != null ){
//			return authFacadeService;
//		}
//		authFacadeService = applicationContext.getBean( DubboAuthFacadeService.class );
//		return authFacadeService;
//	}
}
