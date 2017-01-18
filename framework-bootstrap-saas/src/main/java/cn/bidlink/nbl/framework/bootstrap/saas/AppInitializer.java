package cn.bidlink.nbl.framework.bootstrap.saas;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import cn.bidlink.framework.dao.adapter.DataSourceRequestListener;
import cn.bidlink.nbl.framework.auth.session.SessionOutListener;
import cn.bidlink.nbl.framework.bootstrap.saas.config.BootstrapConfig;
import cn.bidlink.nbl.framework.springmvc.config.SpringmvcConfig;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{ BootstrapConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{ SpringmvcConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/" };
	}
	
	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		//To allow session-scoped beans in Spring
		servletContext.addListener(RequestContextListener.class);
		//Spring 刷新Introspector防止内存泄露
		servletContext.addListener(IntrospectorCleanupListener.class);
		servletContext.addListener(SessionOutListener.class);
		servletContext.addListener(DataSourceRequestListener.class);
		
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		FilterRegistration.Dynamic encodingFilterRegistration = servletContext.addFilter("encodingFilter", encodingFilter);
		encodingFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
		DelegatingFilterProxy shiroFilter = new DelegatingFilterProxy();
		shiroFilter.setTargetFilterLifecycle(true);
		shiroFilter.setTargetBeanName("systemShiroFilter");
		FilterRegistration.Dynamic shiroFilterRegistration = servletContext.addFilter("systemShiroFilter", shiroFilter);
		shiroFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
		DelegatingFilterProxy dataRouteFilter = new DelegatingFilterProxy();
		dataRouteFilter.setTargetFilterLifecycle(true);
		dataRouteFilter.setTargetBeanName("dataRouteFilter");
		FilterRegistration.Dynamic dataRouteFilterRegistration = servletContext.addFilter("dataRouteFilter", dataRouteFilter);
		dataRouteFilterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
		
		super.onStartup(servletContext);
	}
}