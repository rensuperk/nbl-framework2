package cn.bidlink.nbl.framework.beetl;

import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeetlConfig implements EnvironmentAware {
	
	private String templatePath;

	@Override
	public void setEnvironment(Environment env) {
		this.templatePath = env.getProperty("beetl.template.path");
	}

	@Bean
	public BeetlSpringViewResolver beetlViewResolver(){
		BeetlGroupUtilConfiguration config = new BeetlGroupUtilConfiguration();
		config.setResourceLoader(new ClasspathResourceLoader(templatePath));
		config.init();
		BeetlSpringViewResolver resolver = new BeetlSpringViewResolver();
		resolver.setConfig(config);
		resolver.setSuffix(".btl");
		resolver.setContentType("text/html;charset=UTF-8");
		return resolver;
	}
}