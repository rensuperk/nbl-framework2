package cn.bidlink.nbl.framework.mq.activemq;

import java.lang.reflect.Proxy;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.bidlink.nbl.framework.mq.core.MQ;
import cn.bidlink.nbl.framework.mq.core.MQProducer;

public class ActiveMQProducerFactoryBean implements FactoryBean<MQProducer<?>>, InitializingBean {

	private final ConnectionFactory connectionFactory;
	private final String className;
	private Class<?> targetClass;
	
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	
	public ActiveMQProducerFactoryBean(ConnectionFactory connectionFactory, String className){
		this.connectionFactory = connectionFactory;
		this.className = className;
		try {
			this.targetClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = null;
			switch(destinationType){
			case QUEUE:
				destination = session.createQueue(destinationName);
				break;
			case TOPIC:
				destination = session.createTopic(destinationName);
				break;
			}
			consumer = session.createConsumer(destination);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MQProducer<?> getObject() throws Exception {
		Class<?> clazz = getObjectType();
		MQ mq = clazz.getAnnotation(MQ.class);
		
		ActiveMQProducer<?> producer = new ActiveMQProducer<>(connectionFactory, mq.destinationType(), mq.destinationName());
		ActiveMQProducerInvocationHandler h = new ActiveMQProducerInvocationHandler(producer);
		return (MQProducer<?>) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
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