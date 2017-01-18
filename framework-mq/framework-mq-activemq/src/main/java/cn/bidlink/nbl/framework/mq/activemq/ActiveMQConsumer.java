package cn.bidlink.nbl.framework.mq.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;

import com.alibaba.fastjson.JSON;

import cn.bidlink.nbl.framework.mq.core.MQConsumer;

public class ActiveMQConsumer<T> implements MQConsumer<T> {

	private final ConnectionFactory connectionFactory;
	private final DestinationType destinationType;
	private final String destinationName;
	private final Class<T> messageClass;
	
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;
	
	public ActiveMQConsumer(ConnectionFactory connectionFactory, 
			DestinationType destinationType, String destinationName, 
			Class<T> messageClass){
		this.connectionFactory = connectionFactory;
		this.destinationType = destinationType;
		this.destinationName = destinationName;
		this.messageClass = messageClass;
		init();
	}
	
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
			consumer = session.createConsumer(destination);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public T receive() {
		try {
			TextMessage message = (TextMessage)consumer.receive();
//			Message message = consumer.receive();
//			System.out.println(message.toString());
			return JSON.parseObject(message.getText(), messageClass);
		} catch (JMSException e) {
			e.printStackTrace();
		} finally{
			try {
				session.commit();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}