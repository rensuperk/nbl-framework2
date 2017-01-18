package cn.bidlink.nbl.framework.mq.activemq.config;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.bidlink.nbl.framework.mq.activemq.ActiveMQBeanDefinitionRegistryPostProcessor;
import cn.bidlink.nbl.framework.mq.activemq.ActiveMQTransationalInterceptor;

@Configuration
public class ActiveMQConfig implements EnvironmentAware, ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	private String scanPackages;
	private String brokerUrl;
	private String username;
	private String password;
	private String desitions;
	private Integer acknowledgeMode;
	private Boolean autoStartup;
	private Integer cacheLevel;
	
	@Override
	public void setEnvironment(Environment env) {
		this.scanPackages = env.getProperty("activemq.scan.packages");
		this.brokerUrl = env.getProperty("activemq.broker.url");
		this.username = env.getProperty("activemq.username");
		this.password = env.getProperty("activemq.password");
		this.desitions = env.getProperty("activemq.desitions");
		this.acknowledgeMode = env.getProperty("activemq.acknowledge.mode", Integer.class);
		this.autoStartup = env.getProperty("activemq.startup.auto", Boolean.class);
		this.cacheLevel = env.getProperty("activemq.cache.level", Integer.class);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean
	ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(brokerUrl);
		factory.setUserName(username);
		factory.setPassword(password);
//		factory.setTransportListener(new BidJmsTransportListener());
//		factory.setExceptionListener(new BidJmsExceptionListener());
		return factory;
	}
	
	@Bean
	ActiveMQBeanDefinitionRegistryPostProcessor activeMQBeanDefinitionRegistryPostProcessor(){
		ActiveMQConnectionFactory connectionfactory = applicationContext.getBean(ActiveMQConnectionFactory.class);
		ActiveMQBeanDefinitionRegistryPostProcessor processor = new ActiveMQBeanDefinitionRegistryPostProcessor(connectionfactory);
		processor.setScanPackages(scanPackages);
		return processor;
	}
	
	@Bean
	ActiveMQTransationalInterceptor activeMQTransationalInterceptor(){
		return new ActiveMQTransationalInterceptor();
	}
}