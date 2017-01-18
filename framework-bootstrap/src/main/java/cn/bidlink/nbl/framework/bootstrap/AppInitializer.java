package cn.bidlink.nbl.framework.bootstrap;

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

import cn.bidlink.nbl.framework.auth.session.SessionOutListener;
import cn.bidlink.nbl.framework.bootstrap.config.BootstrapConfig;
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
		
		super.onStartup(servletContext);
	}
}