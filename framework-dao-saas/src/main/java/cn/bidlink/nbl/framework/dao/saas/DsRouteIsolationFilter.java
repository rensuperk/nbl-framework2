package cn.bidlink.nbl.framework.dao.saas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;

import cn.bidlink.framework.dao.adapter.CommonColumns;
import cn.bidlink.framework.dao.adapter.DbColumn;
import cn.bidlink.framework.dao.adapter.DsExpendColumnContext;
import cn.bidlink.nbl.user.UserContext;

public class DsRouteIsolationFilter implements Filter {
	
	private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
	
	private String[] excludePaths;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		boolean isMatches = true;
		if(excludePaths != null){
			for(String excludePath : excludePaths){
				if(PATH_MATCHER.match(excludePath, req.getRequestURI())){
					isMatches = false;
					break;
				}
			}
		}
		if(isMatches){
			DbColumn dc = new DbColumn()
			.setClassType(String.class)
			.setColumnName("TENANT_ID")
			.setColumnValue(UserContext.getCurrentUser().getTenantId());
			DsExpendColumnContext.set(new CommonColumns().setDefaultColumn(dc));
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		DsExpendColumnContext.remove();
	}

	public String[] getExcludePaths() {
		return excludePaths;
	}
	public void setExcludePaths(String[] excludePaths) {
		this.excludePaths = excludePaths;
	}
}