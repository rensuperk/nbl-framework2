package cn.bidlink.nbl.framework.auth.access;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class PathAccessDecisionService implements AccessDecisionService {
	public final Logger logger = LoggerFactory.getLogger( getClass() );
	private Set<String> whitePaths = new HashSet<String>();
	@Override
	public boolean isAllow(HttpServletRequest httpServletRequest) {
		boolean isWhite =  false ;
		String servletPath = httpServletRequest.getServletPath();
		if( logger.isDebugEnabled()){
			logger.debug("URLAddress================="+ servletPath );
		}
		out:if( whitePaths!= null && whitePaths.size() > 0 ){
			Iterator<String> whitePathIter = whitePaths.iterator();
			while( whitePathIter.hasNext() ){
				isWhite = servletPath.matches( whitePathIter.next() );
				if( isWhite ){
					break out;
				}
			}
		}
		return isWhite;
	}
	
	public Set<String> getWhitePaths() {
		return whitePaths;
	}
	public void setWhitePaths(Set<String> whitePaths) {
		this.whitePaths = whitePaths;
	}
}
