package cn.bidlink.nbl.framework.mq.activemq;

import javax.jms.ConnectionFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public class ActiveMQBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private ConnectionFactory connectionFactory;
	
	private String scanPackages;
	
	public ActiveMQBeanDefinitionRegistryPostProcessor(ConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ActiveMQBeanDefinitionScanner scanner = new ActiveMQBeanDefinitionScanner(registry);
		scanner.setConnectionFactory(connectionFactory);
		scanner.scan(scanPackages);
	}

	public String getScanPackages() {
		return scanPackages;
	}
	public void setScanPackages(String scanPackages) {
		this.scanPackages = scanPackages;
	}
}