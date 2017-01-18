package cn.bidlink.nbl.framework.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tk.mybatis.mapper.entity.Example;
import cn.bidlink.nbl.framework.bootstrap.config.BootstrapConfig;
import cn.bidlink.nbl.framework.mybatis.dao.UserDao;
import cn.bidlink.nbl.framework.mybatis.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BootstrapConfig.class)
public class MyBatisDaoTest {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void testInsert(){
		User user = new User();
		user.setName("MyBatisDaoTest.testInsert");
		userDao.insert(user);
	}
	
	@Test
	public void testSelect(){
		User user = userDao.selectByPrimaryKey(1);
		System.out.println(user.getName());
	}
}