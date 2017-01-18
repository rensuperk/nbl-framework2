package cn.bidlink.nbl.framework.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bidlink.framework.jms.JmsService;
import cn.bidlink.nbl.framework.bootstrap.config.BootstrapConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BootstrapConfig.class)
public class JmsTest {

	@Autowired
	private JmsService jmsService;

	@Test
	public void testProduce(){
		
	}
	
	@Test
	public void testConsume(){
		
	}
}