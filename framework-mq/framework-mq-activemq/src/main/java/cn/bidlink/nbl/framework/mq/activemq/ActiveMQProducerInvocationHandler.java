package cn.bidlink.nbl.framework.mq.activemq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;

public class ActiveMQProducerInvocationHandler implements InvocationHandler {
	
	private final ConnectionFactory connectionFactory;
	private final DestinationType destinationType;
	private final String destinationName;
	
	public ActiveMQProducerInvocationHandler(ConnectionFactory connectionFactory,
			DestinationType destinationType, String destinationName){
		this.connectionFactory = connectionFactory;
		this.destinationType = destinationType;
		this.destinationName = destinationName;
	}
	
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	
	private void init(){
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
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getName().equals("send")){
			
		}
		return method.invoke(producer, args);
	}
}