package cn.bidlink.nbl.framework.jms.service.impl;

import javax.jms.Message;

import cn.bidlink.framework.jms.BidMessageHandler;
import cn.bidlink.framework.jms.listener.BidQueueAsynMsgListener;

public class UserMQConsumer implements BidMessageHandler {

	@Override
	public void handlerMessage(Message message) {
		BidQueueAsynMsgListener
	}

	@Override
	public int getTimeInterval() {
		return 0;
	}
}