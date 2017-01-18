package cn.bidlink.nbl.framework.mybatis;

import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bidlink.nbl.framework.bootstrap.config.BootstrapConfig;
import cn.bidlink.nbl.framework.mybatis.dao.UserDao;
import cn.bidlink.nbl.framework.mybatis.model.User;
import cn.bidlink.nbl.framework.mybatis.schema.DbColumn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BootstrapConfig.class)
public class MyBatisDefaultColumnTest {
	
	@Autowired
	private UserDao userDao;
	
	@Before
	public void doBefore(){
		DbColumn dbColumn = new DbColumn();
		dbColumn.setColumn(new Column("TENANT_ID"));
		dbColumn.setExpression(new StringValue("test"));
		MyBatisContext.addDefaultColumn(dbColumn);
	}
	
	@Test
	public void testInsert(){
		User user = new User();
		user.setName("MyBatisDefaultColumnTest.testInsert");
		userDao.insert(user);
	}
}