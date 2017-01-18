package cn.bidlink.nbl.framework.jms.config;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;

import cn.bidlink.framework.jms.JmsService;
import cn.bidlink.framework.jms.impl.JmsServiceImpl;
import cn.bidlink.framework.jms.listener.BidJmsExceptionListener;
import cn.bidlink.framework.jms.listener.BidJmsTransportListener;

@Configuration
public class JmsConfig implements EnvironmentAware {
	
	private String brokerUrl;
	private String username;
	private String password;
	
	@Override
	public void setEnvironment(Environment env) {
		this.brokerUrl = env.getProperty("mq.con.brokerURL");
		this.username = env.getProperty("mq.con.username");
		this.password = env.getProperty("mq.con.password");
	}

	@Bean(name="jmsConnectionFactory")
	ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL(brokerUrl);
		factory.setUserName(username);
		factory.setPassword(password);
		factory.setTransportListener(new BidJmsTransportListener());
		factory.setExceptionListener(new BidJmsExceptionListener());
		return factory;
	}
	
	@Bean
	JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(activeMQConnectionFactory());
		return template;
	}
	
	@Bean
	JmsService jmsService(){
		JmsServiceImpl service = new JmsServiceImpl();
		service.setJmsTemplate(jmsTemplate());
		return service;
	}
}