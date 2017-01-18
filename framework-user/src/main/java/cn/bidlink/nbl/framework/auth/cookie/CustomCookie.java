package cn.bidlink.nbl.framework.auth.cookie;

import org.apache.shiro.web.servlet.SimpleCookie;

public class CustomCookie extends SimpleCookie {

	@Override
	protected String buildHeaderValue(String name, String value, String comment, String domain, String path, int maxAge,
			int version, boolean secure, boolean httpOnly) {
		path="";
		return super.buildHeaderValue(name, value, comment, domain, path, maxAge, version, secure, httpOnly);
	}


	
}
