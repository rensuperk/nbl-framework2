package cn.bidlink.nbl.framework.mq.activemq;

import java.io.IOException;
import java.util.Set;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;

import cn.bidlink.nbl.framework.mq.core.MQ;
import cn.bidlink.nbl.framework.mq.core.MQConsumer;
import cn.bidlink.nbl.framework.mq.core.MQProducer;

public class ActiveMQBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
	
	private ConnectionFactory connectionFactory;
	
	public ActiveMQBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}

	@Override
	protected boolean isCandidateComponent(MetadataReader metadataReader)
			throws IOException {
		String className = metadataReader.getClassMetadata().getClassName();
		try {
			Class<?> clazz = Class.forName(className);
			return clazz.getAnnotation(ActiveMQ.class) != null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return true;
	}
	
	@Override
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		Set<BeanDefinition> definitions = super.findCandidateComponents(basePackage);
		for(BeanDefinition definition : definitions){
			String targetBeanName = definition.getBeanClassName();
			definition.setLazyInit(false);
			Class<?> targetBeanClass = null;
			try {
				targetBeanClass = Class.forName(targetBeanName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			for(Class<?> targetParentClass : targetBeanClass.getInterfaces()){
				if(targetParentClass.equals(MQProducer.class)){
					definition.setBeanClassName(ActiveMQProducerFactoryBean.class.getName());
				}
				if(targetParentClass.equals(MQConsumer.class)){
					definition.setBeanClassName(ActiveMQConsumerFactoryBean.class.getName());
				}
			}
			definition.getConstructorArgumentValues().addIndexedArgumentValue(0, connectionFactory);
			definition.getConstructorArgumentValues().addIndexedArgumentValue(1, targetBeanName);
		}
		return definitions;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}