package cn.bidlink.nbl.framework.auth.utils;
 
import java.util.HashMap;  
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils; 
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import cn.bidlink.nbl.authority.model.DataAuthCondition;
import cn.bidlink.nbl.framework.auth.dto.SubjectDto;
import cn.bidlink.nbl.user.core.enums.CommonEnum;
import cn.bidlink.nbl.user.model.User;

public class AuthUtils {
	
	/**
	 * 获取登录用户
	 * @see cn.bidlink.nbl.framework.core.context.UserContext
	 */
	@Deprecated
	public static User getLoginUser(HttpServletRequest request){
		return (User)request.getSession().getAttribute(AuthContent.USER_LOGIN);
	}
	/**
	 * @see cn.bidlink.nbl.framework.core.context.UserContext
	 * */
	@Deprecated
	public static User getLoginUser(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return getLoginUser( request );
	}
	/**
	 * 获取 审核发起人
	 * @return
	 */
	public static User getApplyUser(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return (User)request.getAttribute( AuthContent.USER_APPLY );
	}
	/**
	 * 获取登录用户对应的租户
	 * 过时原因：可以通过getLoginUser().getTenatnId()
	 * @param request
	 * @return
	 */
	@Deprecated
	public static  User getTenantUser(HttpServletRequest request){
		return (User) request.getSession().getAttribute(AuthContent.USER_TENANT);
	}
	/**
	 * 过时原因：可以通过getLoginUser().getTenatnId()
	 * @return
	 */
	@Deprecated
	public static  User getTenantUser(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return getTenantUser( request );
	}
	/**
	 * 获取数据权
	 * @param request
	 * @return
	 */
	public static DataAuthCondition getDataAuthCondition(HttpServletRequest request){
		return (DataAuthCondition) request.getAttribute( AuthContent.AUTH_CONDITION );
	}
	public static DataAuthCondition getDataAuthCondition(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return getDataAuthCondition( request );
	}
	
	@SuppressWarnings({ "unchecked" })
	public static SubjectDto getSubjectDto(){
		Subject subject = SecurityUtils.getSubject();
		HashMap<String, String> subjectMap =  (HashMap<String, String>)subject.getPrincipals().asSet().toArray(new Object[2])[1];
		return new SubjectDto( subjectMap );
	}
	/**
	 * 
	 * @param user
	 * @return true 主账号｜租户 ，false 子账号	 
	 */
	public static boolean isTenant( User user){
		if( user == null ){
			return false;
		}
		if(user.getTenantId() != null && StringUtils.equals( user.getId(), user.getTenantId())){
			return true ;
		}
		if(user.getIsSubject() != null && CommonEnum.IS_SUBJECT.getValue() == user.getIsSubject() ){
			return true ;
		}
		return false ;
	}
	
}
