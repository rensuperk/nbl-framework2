package cn.bidlink.nbl.framework.mq.activemq;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.FactoryBean;

import cn.bidlink.nbl.framework.mq.core.MQ;
import cn.bidlink.nbl.framework.mq.core.MQConsumer;

public class ActiveMQConsumerFactoryBean implements FactoryBean<MQConsumer<?>> {
	
	private final ConnectionFactory connectionFactory;
	private final String className;

	public ActiveMQConsumerFactoryBean(ConnectionFactory connectionFactory, String className){
		this.connectionFactory = connectionFactory;
		this.className = className;
	}
	
	@Override
	public MQConsumer<?> getObject() throws Exception {
		Class<?> clazz = getObjectType();
		ActiveMQ mq = clazz.getAnnotation(ActiveMQ.class);
		Class<?> messageClass = (Class<?>) ((ParameterizedType)clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
		ActiveMQConsumer<?> consumer = new ActiveMQConsumer<>(connectionFactory, mq.destinationType(), mq.destinationName(), messageClass);
		ActiveMQConsumerInvocationHandler h = new ActiveMQConsumerInvocationHandler(consumer);
		return (MQConsumer<?>) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
	}

	@Override
	public Class<?> getObjectType() {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}