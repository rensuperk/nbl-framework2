package cn.bidlink.nbl.framework.auth.handler;

import java.util.Iterator; 
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse; 
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.bidlink.nbl.framework.auth.utils.AuthContent;

/**
 * 负责Cas 退出及 通知其他App进行退出 
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年9月17日 下午2:06:57 
 */
//@Component(value="casLogoutHandler")
public class CasLogoutHandler extends LogoutFilter {  
	
	private String logoutParameterName = "logoutRequest";
	private static final Logger log = LoggerFactory.getLogger(CasLogoutHandler.class);
	
	@Autowired
	private RedisSessionDAO redisSessionDAO;
	
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    	return super.preHandle(request, response);
    }
    
	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if( isLogoutRequest( httpServletRequest ) ){
			final String logoutMessage = CommonUtils.safeGetParameter(httpServletRequest, this.logoutParameterName);
	        if( log.isDebugEnabled() ){
	        	log.debug("logout message ==============="+logoutMessage );
	        }
	        final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
	        if( log.isDebugEnabled() ){
	        	log.debug( "logout token ==============="+token );
	        }
	        if (CommonUtils.isNotBlank(token)) {
	        	try {
	        		Iterator<Session> sessionIterator = getRedisSessionDAO().getActiveSessions().iterator();
		        	while( sessionIterator.hasNext() ){
		        		Session sessionNext = sessionIterator.next();		        		
		        		Object faodfa = sessionNext.getAttribute( AuthContent.TGT_KEY );
		        		if( faodfa == null ){
		        			continue;
		        		}
		        		if( StringUtils.equals( (String)faodfa , token)){
		        			if( log.isDebugEnabled() ){
		        	        	log.debug( "deleteId =========="+sessionNext.getId() );
		        	        }
		        			getRedisSessionDAO().delete( sessionNext );
		        			break ;
		        		}
		        	}
				} catch (Exception e) {
					log.error("logout error" , e );
				} 
	        } 
		}
		super.postHandle(request, response);
	} 
	
	public boolean isLogoutRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && !isMultipartRequest(request) &&
            CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName));
    }
	
	private boolean isMultipartRequest(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().toLowerCase().startsWith("multipart");
    }
	public RedisSessionDAO getRedisSessionDAO() {
		return redisSessionDAO;
	}
	public void setRedisSessionDAO(RedisSessionDAO redisSessionDAO) {
		this.redisSessionDAO = redisSessionDAO;
	}
}