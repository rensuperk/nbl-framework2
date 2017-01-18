package cn.bidlink.nbl.framework.mq.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import cn.bidlink.nbl.framework.mq.core.DestinationType;
import cn.bidlink.nbl.framework.mq.core.MQProducer;

import com.alibaba.fastjson.JSON;

public class ActiveMQProducer<T> implements MQProducer<T> {

	private final ConnectionFactory connectionFactory;
	private final DestinationType destinationType;
	private final String destinationName;
	
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	
	public ActiveMQProducer(ConnectionFactory connectionFactory, DestinationType destinationType,
			String destinationName){
		this.connectionFactory = connectionFactory;
		this.destinationType = destinationType;
		this.destinationName = destinationName;
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
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void send(T object) {
		try {
			String text = JSON.toJSONString(object);
			TextMessage message = ActiveMQContext.getCurrentSession().createTextMessage(text);
			producer.send(message);
//			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}