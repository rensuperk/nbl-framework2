package cn.bidlink.nbl.framework.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableTransactionManagement
@PropertySource("classpath:bootstrap.properties")
@ComponentScan(basePackages = "${scan.basePackage}")
public class BootstrapConfig {

	@Bean
	PropertyPlaceholderConfigurer propertyConfigurer(){
		PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
		propertyConfigurer.setFileEncoding("UTF-8");
		propertyConfigurer.setLocation(new ClassPathResource("bootstrap.properties"));
		return propertyConfigurer;
	}
	
	/**
	 * HTTP JSON 处理
	 * */
	@Bean
	MappingJackson2HttpMessageConverter httpMessageConverter(){
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(mediaTypes);
		return converter;
	}
	
	/**
	 * HTML 处理
	 * */
	
}