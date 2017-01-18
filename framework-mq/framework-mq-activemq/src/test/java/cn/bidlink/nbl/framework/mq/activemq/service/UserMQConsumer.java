package cn.bidlink.nbl.framework.mq.activemq.service;

import cn.bidlink.nbl.framework.mq.activemq.model.User;
import cn.bidlink.nbl.framework.mq.core.MQ;
import cn.bidlink.nbl.framework.mq.core.MQConsumer;

@MQ(destinationName="")
public interface UserMQConsumer extends MQConsumer<User> {

}