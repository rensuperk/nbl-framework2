package cn.bidlink.nbl.framework.springmvc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import cn.bidlink.nbl.framework.springmvc.RequestBodyParamHandlerMethodArgumentResolver;
import cn.bidlink.nbl.framework.springmvc.RespHandlerExceptionResolver;
import cn.bidlink.nbl.framework.springmvc.RespReturnValueHandler;

@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(basePackages = "${scan.basePackage}", 
	useDefaultFilters=false, 
	includeFilters={
		@Filter(Controller.class),
		@Filter(RestController.class)
	}
)
public class SpringmvcConfig extends WebMvcConfigurationSupport {
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
		argumentResolvers.add(new RequestBodyParamHandlerMethodArgumentResolver());
		adapter.setCustomArgumentResolvers(argumentResolvers);
		List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();
		returnValueHandlers.add(respReturnValueHandler());
		returnValueHandlers.add(new ModelAndViewMethodReturnValueHandler());
		adapter.setReturnValueHandlers(returnValueHandlers);
		return adapter;
	}
	
	@Bean
	public RespReturnValueHandler respReturnValueHandler(){
		RespReturnValueHandler handler = new RespReturnValueHandler();
		return handler;
	}
	
	@Bean
	public RespHandlerExceptionResolver respHandlerExceptionResolver(){
		RespHandlerExceptionResolver resolver = new RespHandlerExceptionResolver();
		return resolver;
	}
}