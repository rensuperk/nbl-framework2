package cn.bidlink.nbl.framework.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.bidlink.framework.core.annotation.Dao;
import cn.bidlink.framework.core.support.properties.BidPropertySourcesPlaceholderConfigrer;

@EnableAspectJAutoProxy(proxyTargetClass=true)
@PropertySource("classpath:bootstrap.properties")
@ComponentScan(basePackages = "${scan.basePackage}", 
	useDefaultFilters=false, 
	includeFilters={
		@Filter(Configuration.class),
		@Filter(Dao.class),
		@Filter(Service.class)
	}
)
public class BootstrapConfig {

	@Bean
	public BidPropertySourcesPlaceholderConfigrer bidPropertyConfig(){
		BidPropertySourcesPlaceholderConfigrer bean = new BidPropertySourcesPlaceholderConfigrer();
		bean.setFileEncoding("UTF-8");
		bean.setLocations(new ClassPathResource("bootstrap.properties"));
		return bean;
	}
}