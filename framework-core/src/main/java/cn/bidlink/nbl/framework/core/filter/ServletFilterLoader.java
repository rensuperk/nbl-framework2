//package cn.bidlink.nbl.framework.core.filter;
//
//import java.util.EnumSet;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.servlet.DispatcherType;
//import javax.servlet.Filter;
//import javax.servlet.FilterRegistration;
//import javax.servlet.ServletContext;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.web.context.ServletContextAware;
//import org.springframework.web.filter.DelegatingFilterProxy;
//
//public class ServletFilterLoader implements BeanFactoryPostProcessor, ServletContextAware {
//	
//	private ServletContext servletContext;
//	
//	@Override
//	public void setServletContext(ServletContext servletContext) {
//		this.servletContext = servletContext;
//	}
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
//			throws BeansException {
//		Map<String, Filter> filterMap = factory.getBeansOfType(Filter.class);
//		for(Entry<String, Filter> entry : filterMap.entrySet()){
//			String beanName = entry.getKey();
//			Filter filter = entry.getValue();
//			DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
//			filterProxy.setTargetFilterLifecycle(true);
//			filterProxy.setTargetBeanName(beanName);
//			FilterRegistration.Dynamic filterRegistration = servletContext.addFilter(beanName, (Filter) filter);
//			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
//		}
//	}
//}