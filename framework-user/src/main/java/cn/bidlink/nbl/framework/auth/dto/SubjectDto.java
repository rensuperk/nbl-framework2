package cn.bidlink.nbl.framework.auth.dto;

import java.util.HashMap;
public class SubjectDto {
	private HashMap<String, String> hashMap ;
	
	public SubjectDto( HashMap<String, String> hashMap){
		this.hashMap = hashMap ;
	}
	public String getUserName() {
		return hashMap.get("username");
	}
	public boolean isAuthorize() {
		return Boolean.valueOf( hashMap.get( "authorize") );
	} 
	public String getGrantingTicket() {
		return hashMap.get("grantingTicket");
	}
	public String getRealService() {
		return hashMap.get("realService");
	} 
	public String getLoginUrl() {
		return hashMap.get("loginUrl");
	}
	public String getService() {
		return hashMap.get("service");
	} 
	public String getReference() {
		return hashMap.get("domainName");
	}
	
}
