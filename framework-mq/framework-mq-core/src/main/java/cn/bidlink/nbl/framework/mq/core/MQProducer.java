package cn.bidlink.nbl.framework.mq.core;

public interface MQProducer<T> {
	
	public void send(T object);

}