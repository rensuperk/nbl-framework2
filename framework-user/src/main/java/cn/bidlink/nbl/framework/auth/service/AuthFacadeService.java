package cn.bidlink.nbl.framework.auth.service;
 
import cn.bidlink.nbl.authority.service.AuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleAuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleOrganizationRemoteService;
import cn.bidlink.nbl.authority.service.MidUserRoleRemoteService;
import cn.bidlink.nbl.authority.service.ResourceRemoteService;
import cn.bidlink.nbl.user.service.OrganizationRemoteService;
import cn.bidlink.nbl.user.service.UserRemoteService;


public interface AuthFacadeService {
	public OrganizationRemoteService getOrganizationRemoteService();
	public UserRemoteService getUserRemoteService();
	public ResourceRemoteService getResourceRemoteService();
	public AuthorityRemoteService getAuthorityRemoteService();
	public MidUserRoleRemoteService getMidUserRoleRemoteService();
	public MidRoleAuthorityRemoteService getMidRoleAuthorityRemoteService();
	public MidRoleOrganizationRemoteService getMidRoleOrganizationRemoteService();
	
}
