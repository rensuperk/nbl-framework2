package cn.bidlink.nbl.framework.auth.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @version : Ver 1.0
 * @author	: <a href="mailto:yijundai@ebnew.com">yijundai</a>
 * @date	: 2015年10月10日 上午11:00:23 
 */
public class PathUtils {
	private static Logger LOGGER = LoggerFactory.getLogger( PathUtils.class );
	/**
	 * 目前仅 支持 xxx/get/${id} 路径的rest风格
	 * 数据库录入此rest 路径时 需要在get后面添加上/符号，表明 不是最后一级路径
	 */
	public static String getServletPath( HttpServletRequest httpServletRequest ){
		String restPath = httpServletRequest.getServletPath() ;
		if( "GET".equalsIgnoreCase( httpServletRequest.getMethod() ) ){ 
			if( restPath.matches( ".*/get/[^/]+") ){
				restPath = restPath.substring( 0 , restPath.lastIndexOf("/")+1 );
			}
		}
		return restPath;
	}
	
	public static String getDomainName( HttpServletRequest httpServletRequest ){
		if( httpServletRequest ==null ){
			return "nbl.ebnew.com";
		}
		String servletName = httpServletRequest.getServerName();
		if("127.0.0.1".equals( servletName )){
			servletName ="localnbl.ebnew.com";
		}
		if( LOGGER.isDebugEnabled() ){
			LOGGER.debug("servletName==============="+servletName );
		}
		return servletName;
	}
}
