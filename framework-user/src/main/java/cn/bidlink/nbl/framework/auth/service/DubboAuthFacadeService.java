package cn.bidlink.nbl.framework.auth.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.bidlink.nbl.authority.service.AuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleAuthorityRemoteService;
import cn.bidlink.nbl.authority.service.MidRoleOrganizationRemoteService;
import cn.bidlink.nbl.authority.service.MidUserRoleRemoteService;
import cn.bidlink.nbl.authority.service.ResourceRemoteService;
import cn.bidlink.nbl.user.service.OrganizationRemoteService;
import cn.bidlink.nbl.user.service.UserRemoteService;

//@Component
public class DubboAuthFacadeService implements AuthFacadeService{
	
	@Reference
	private OrganizationRemoteService organizationRemoteService;
	@Reference
	private UserRemoteService userRemoteService;
	@Reference
	private ResourceRemoteService resourceRemoteService;
	@Reference
	private AuthorityRemoteService authorityRemoteService;
	@Reference
	private MidUserRoleRemoteService midUserRoleRemoteService;
	@Reference
	private MidRoleAuthorityRemoteService midRoleAuthorityRemoteService;
	@Reference
	private MidRoleOrganizationRemoteService midRoleOrganizationRemoteService;
	
	public DubboAuthFacadeService(){
		System.out.println("----");
	}
	
	public OrganizationRemoteService getOrganizationRemoteService() {
		return organizationRemoteService;
	}
	public void setOrganizationRemoteService(
			OrganizationRemoteService organizationRemoteService) {
		this.organizationRemoteService = organizationRemoteService;
	}
	public UserRemoteService getUserRemoteService() {
		return userRemoteService;
	}
	public void setUserRemoteService(UserRemoteService userRemoteService) {
		this.userRemoteService = userRemoteService;
	}
	public ResourceRemoteService getResourceRemoteService() {
		return resourceRemoteService;
	}
	public void setResourceRemoteService(ResourceRemoteService resourceRemoteService) {
		this.resourceRemoteService = resourceRemoteService;
	}
	public AuthorityRemoteService getAuthorityRemoteService() {
		return authorityRemoteService;
	}
	public void setAuthorityRemoteService(
			AuthorityRemoteService authorityRemoteService) {
		this.authorityRemoteService = authorityRemoteService;
	}
	public MidUserRoleRemoteService getMidUserRoleRemoteService() {
		return midUserRoleRemoteService;
	}
	public void setMidUserRoleRemoteService(
			MidUserRoleRemoteService midUserRoleRemoteService) {
		this.midUserRoleRemoteService = midUserRoleRemoteService;
	}
	public MidRoleAuthorityRemoteService getMidRoleAuthorityRemoteService() {
		return midRoleAuthorityRemoteService;
	}
	public void setMidRoleAuthorityRemoteService(
			MidRoleAuthorityRemoteService midRoleAuthorityRemoteService) {
		this.midRoleAuthorityRemoteService = midRoleAuthorityRemoteService;
	}
	public MidRoleOrganizationRemoteService getMidRoleOrganizationRemoteService() {
		return midRoleOrganizationRemoteService;
	}
	public void setMidRoleOrganizationRemoteService(
			MidRoleOrganizationRemoteService midRoleOrganizationRemoteService) {
		this.midRoleOrganizationRemoteService = midRoleOrganizationRemoteService;
	}
}