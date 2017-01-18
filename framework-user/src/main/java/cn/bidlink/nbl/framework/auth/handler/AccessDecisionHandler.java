package cn.bidlink.nbl.framework.auth.handler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.bidlink.nbl.framework.auth.access.AccessDecisionService;

/**
 * 
 * 处理白名单
 *
 */
public class AccessDecisionHandler extends AdviceFilter implements ApplicationContextAware {
	
	private ApplicationContext applicationContext; 
	private List<AccessDecisionService> whitePathMatchs = new LinkedList<AccessDecisionService>();
	
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		if( whitePathMatchs.size() == 0 ){
			initList();
		}
		//验证信息
		boolean isWhiteRequest = false ;
		Iterator<AccessDecisionService> templateElementIter = whitePathMatchs.iterator();
		while( templateElementIter.hasNext() ){
			AccessDecisionService tempWhitePathMatch = templateElementIter.next();
			isWhiteRequest = tempWhitePathMatch.isAllow( (HttpServletRequest) request );
			if( isWhiteRequest == true ){
				break ;
			}
		}
		if( isWhiteRequest == true ){
			request.setAttribute( "authcCheckHandler.FILTERED" , "true");
			request.setAttribute( "authzAssignHandler.FILTERED" , "true");
			request.setAttribute( "authzCheckHandler.FILTERED" , "true");
			request.setAttribute( "casLoginHandler.FILTERED" , "true");
			request.setAttribute( "casLogoutHandler.FILTERED" , "true");
			request.setAttribute( "dataAuthConditionHandler.FILTERED" , "true");
			request.setAttribute( "whiteRequestHandler.FILTERED" , "true");
			request.setAttribute( "threadLocalSetHandler.FILTERED" , "true");
		}
		return true ;
	}
	
	private synchronized void initList(){
		if( whitePathMatchs.size() > 0 ){
			return ;
		} 
		Map<String, AccessDecisionService> maps = applicationContext.getBeansOfType( AccessDecisionService.class );
		if( maps == null || maps.size() == 0 ){
			return ;
		}
		Iterator<AccessDecisionService> valids = maps.values().iterator();
		while( valids.hasNext() ){
			whitePathMatchs.add( valids.next() );
		}
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
