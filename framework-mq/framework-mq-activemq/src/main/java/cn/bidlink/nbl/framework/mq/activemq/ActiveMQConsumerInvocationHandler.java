package cn.bidlink.nbl.framework.mq.activemq;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ActiveMQConsumerInvocationHandler implements InvocationHandler {
	
	private ActiveMQConsumer<?> consumer;
	
	public ActiveMQConsumerInvocationHandler(ActiveMQConsumer<?> consumer){
		this.consumer = consumer;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(consumer, args);
	}
}