package cn.bidlink.nbl.framework.mq.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bidlink.nbl.framework.bootstrap.config.BootstrapConfig;
import cn.bidlink.nbl.framework.mq.activemq.model.User;
import cn.bidlink.nbl.framework.mq.activemq.service.UserMQConsumer;
import cn.bidlink.nbl.framework.mq.activemq.service.UserMQProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BootstrapConfig.class)
public class ActiveMQTest {
	
	@Autowired
	private UserMQProducer userMQProducer;
	@Autowired
	private UserMQConsumer userMQConsumer;
	
	@Test
	public void testProducer(){
		User user = new User();
		user.setId(1L);
		user.setName("A");
		userMQProducer.send(user);
	}
	
	@Test
	@Scheduled(cron = "0/5 * *  * * ?")
	public void testConsumer(){
		while(true){
			User user = userMQConsumer.receive();
			System.out.println(user.getName());
		}
	}
	
	@Test
	public void testTransactional(){
		
	}
}