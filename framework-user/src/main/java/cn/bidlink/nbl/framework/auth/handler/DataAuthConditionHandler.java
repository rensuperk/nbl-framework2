package cn.bidlink.nbl.framework.auth.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.bidlink.nbl.authority.model.Authority;
import cn.bidlink.nbl.authority.model.DataAuthCondition;
import cn.bidlink.nbl.authority.model.MidRoleAuthority;
import cn.bidlink.nbl.authority.model.MidRoleOrganization;
import cn.bidlink.nbl.authority.model.MidUserRole;
import cn.bidlink.nbl.authority.model.Resource;
import cn.bidlink.nbl.authority.service.AuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleAuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleOrganizationRemoteService;
import cn.bidlink.nbl.authority.service.MidUserRoleRemoteService;
import cn.bidlink.nbl.authority.service.ResourceRemoteService;
import cn.bidlink.nbl.framework.auth.service.AuthFacadeService;
import cn.bidlink.nbl.framework.auth.utils.AuthContent;
import cn.bidlink.nbl.framework.auth.utils.AuthUtils;
import cn.bidlink.nbl.framework.auth.utils.PathUtils;
import cn.bidlink.nbl.user.model.Organization;
import cn.bidlink.nbl.user.model.User;
import cn.bidlink.nbl.user.service.OrganizationRemoteService;
import cn.bidlink.nbl.user.service.UserRemoteService;

/**
 * 对request 进行封装
 * 实现获取 请求路径 对应的数据权（包含本人、本公司、本部门，自定义部门） 
 * 数据权规则：
 * 本公司>本部门，自定义部门>本人
 * 即当存在本公司权时，则其他权限失效
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年10月10日 上午10:53:57 
 */ 
//@Component(value="dataAuthConditionHandler")
public class DataAuthConditionHandler extends OncePerRequestFilter  {
	
//	@Autowired
//	public AuthFacadeService authFacadeService;
	
	@Autowired
	private OrganizationRemoteService organizationRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private ResourceRemoteService resourceRemoteService;
	@Autowired
	private AuthorityRemoteService authorityRemoteService;
	@Autowired
	private MidUserRoleRemoteService midUserRoleRemoteService;
	@Autowired
	private MidRoleAuthorityRemoteService midRoleAuthorityRemoteService;
	@Autowired
	private MidRoleOrganizationRemoteService midRoleOrganizationRemoteService;
	
	private static final String TENANT_KEY = "TENANT_AUTH_SQL_CONDITION";
	private static final String COMPANY_KEY = "COMPANY_AUTH_SQL_CONDITION";
	private static final String DEPARTMENT_KEY = "DEPARTMENT_AUTH_SQL_CONDITION";
	private static final String USER_KEY = "USER_AUTH_SQL_CONDITION";
	
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String path = PathUtils.getServletPath(httpServletRequest);
		Map<String, Object> dataAuthMap = getAuthMap( httpServletRequest , path );
		DataAuthCondition dataAuthCondition = convertToBean( dataAuthMap );
		httpServletRequest.setAttribute( AuthContent.AUTH_CONDITION ,  dataAuthCondition );
		chain.doFilter(httpServletRequest, response);
	}
	
	/**
	 * 1、获取 当前路径所对应的模块资源
	 * 2、获取当前用户的角色
	 * 3、获取当前角色 在此模块下的权限(本人，本公司，本部门)
	 * 4、获取当前角色 在此模块下的自定义权限 
	 * 5、组装数据
	 * @param httpServletRequest
	 * @param path
	 * @return
	 */
	private Map<String, Object> getAuthMap( HttpServletRequest httpServletRequest , String path ){
		//组装数据
		Map<String, Object> authParamMap = new HashMap<String, Object>(10);
		//获取当前用户的角色
		HttpSession httpSession = httpServletRequest.getSession();
		User loginUser =  (User)httpSession.getAttribute( AuthContent.USER_LOGIN );
		//获取 当前路径所对应的模块资源
		Resource r = new Resource();
		r.setUrl( path );
		List<Resource> resourceList = resourceRemoteService.findByCondition( r );
		if( resourceList == null || resourceList.size() == 0 ){
			if( StringUtils.isBlank( loginUser.getTenantId() )){
				authParamMap.put( TENANT_KEY ,  loginUser.getId()  );
			}else{
				authParamMap.put( TENANT_KEY ,  loginUser.getTenantId()  );
			}
			
			return authParamMap;
		}  
		String moduleID = null ;
		for (int i = 0; i < resourceList.size() ; i++) {
			Resource tempResource = resourceList.get( i );
			if( tempResource.getType() != 4 ){
				continue;
			}
			moduleID = tempResource.getPid() ;
			if( moduleID == null || "".equals( moduleID )){
				moduleID =  tempResource.getId() ;
			}
			break ;
		}
		if( moduleID == null ){
			authParamMap.put( TENANT_KEY ,  loginUser.getTenantId()  );
			return authParamMap;
		}
		//验证是否主账号
		if( AuthUtils.isTenant( loginUser ) ){
			authParamMap.put( TENANT_KEY ,  loginUser.getTenantId()  );
			return authParamMap;
		}
		
		MidUserRole mur = new MidUserRole();
		mur.setUserId( loginUser.getId() );
		List<MidUserRole> murList = midUserRoleRemoteService.findByCondition( mur );
		//获取当前角色 在此模块下的权限(本人，本公司，本部门)
		Set<String> authNameSet = new HashSet<String>();
		for (int i = 0; i < murList.size() ; i++) {
			MidUserRole murFor = murList.get( i );
			MidRoleAuthority mra = new MidRoleAuthority();
			mra.setResourceId( moduleID );
			mra.setRoleId( murFor.getRoleId() );
			List<MidRoleAuthority> mraList = midRoleAuthorityRemoteService.findByCondition( mra );
			for (int j = 0; j < mraList.size(); j++) {
				MidRoleAuthority mraFor = mraList.get( j );
				Authority tempAuth = authorityRemoteService.findByPK( mraFor.getAuthorityId() ); 
				if( tempAuth != null && tempAuth.getType() == 3){
					authNameSet.add( tempAuth.getName() );
				}
			}
		} 
		//获取当前角色 在此模块下的自定义权限 
		Set<Long> custorAuthKeySet = new HashSet<Long>();
		for (int i = 0; i < murList.size() ; i++) { 
			MidRoleOrganization mro = new MidRoleOrganization();
			mro.setRoleId( murList.get( i ).getRoleId() );
			mro.setResourceId( moduleID );
			List<MidRoleOrganization>  mroList = midRoleOrganizationRemoteService.findByCondition( mro);
			Iterator<MidRoleOrganization> mroIter = mroList.iterator();
			while( mroIter.hasNext() ){
				String orgID = mroIter.next().getOrgId();
				Organization organWhile = organizationRemoteService.findByPK( orgID );
				if( organWhile != null ){
					custorAuthKeySet.add( organWhile.getAuthKey() );
				}
			}
		}
		
		Organization userOrg = null ;
		if( loginUser.getOrgId() != null ){
			userOrg = organizationRemoteService.findByPK( loginUser.getOrgId() );
		}
		if( authNameSet.contains("本租户")){
			authParamMap.put( TENANT_KEY ,  loginUser.getTenantId() );
		}
		if( authNameSet.contains( "本公司" ) && userOrg != null){
			authParamMap.put( COMPANY_KEY , userOrg.getAuthKey() );
		}
		if( custorAuthKeySet.size() > 0 &&
			authNameSet.contains( "本部门" ) && 
			userOrg != null){
			Long[] departs = new Long[ custorAuthKeySet.size() +1 ];
			departs[0] = userOrg.getAuthKey() ;
			int i = 0 ;
			Iterator<Long> custAuthIter = custorAuthKeySet.iterator();
			while( custAuthIter.hasNext() ){
				++i;
				departs[ i ] = custAuthIter.next() ;
			}
			authParamMap.put( DEPARTMENT_KEY , departs );
		}else if( custorAuthKeySet.size() > 0 ){
			Long[] departs = new Long[ custorAuthKeySet.size() ]; 
			int i = 0 ;
			Iterator<Long> custAuthIter = custorAuthKeySet.iterator();
			while( custAuthIter.hasNext() ){
				departs[ i++ ] = custAuthIter.next() ;
			}
			authParamMap.put( DEPARTMENT_KEY , departs );
		}else if( authNameSet.contains( "本部门" ) &&  userOrg != null){
			authParamMap.put( DEPARTMENT_KEY , new Long[]{ userOrg.getAuthKey() } );
		}
		if( authNameSet.contains( "本人" ) ){
			authParamMap.put( USER_KEY , loginUser.getId() );
		}
		return authParamMap; 
	}
	
 
	
	private DataAuthCondition convertToBean( Map<String, Object> authMap ){
		DataAuthCondition dataAuthCondition = new DataAuthCondition();
		if( authMap == null || authMap.size() == 0 ){ 
			dataAuthCondition.setTenantId( (String)authMap.get( TENANT_KEY ) );
			return dataAuthCondition ;
		}
		if( authMap.containsKey( TENANT_KEY ) ){
			dataAuthCondition.setTenantId( (String)authMap.get( TENANT_KEY ) ); 
		}
		if( authMap.containsKey( COMPANY_KEY ) ){
			dataAuthCondition.setCompanyId( (Long)authMap.get( COMPANY_KEY ) ); 
		}
		if( authMap.containsKey( DEPARTMENT_KEY ) ){
			dataAuthCondition.setDepartmentsId( ( Long[] )authMap.get( DEPARTMENT_KEY ) );
		}
		if( authMap.containsKey( USER_KEY ) ){
			dataAuthCondition.setUserId( (String)authMap.get( USER_KEY ));
		}
		return dataAuthCondition ;
	}
//	public AuthFacadeService getAuthFacadeService() {
//		return authFacadeService;
//	}
//	public void setAuthFacadeService(AuthFacadeService authFacadeService) {
//		this.authFacadeService = authFacadeService;
//	}
}
