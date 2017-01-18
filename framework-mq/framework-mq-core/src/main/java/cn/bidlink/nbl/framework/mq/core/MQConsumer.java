package cn.bidlink.nbl.framework.mq.core;

public interface MQConsumer<T> {

	public T receive();

}