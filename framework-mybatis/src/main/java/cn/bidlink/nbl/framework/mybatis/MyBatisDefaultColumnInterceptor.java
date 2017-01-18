package cn.bidlink.nbl.framework.mybatis;

import java.util.Properties;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import cn.bidlink.nbl.framework.mybatis.schema.DbColumn;

@Intercepts({
	@Signature(type = Executor.class, 
		method = "query", 
		args = {
			MappedStatement.class,
			Object.class,
			RowBounds.class,
			ResultHandler.class
		}
	),
	@Signature(type = Executor.class, 
		method = "update", 
		args = {
			MappedStatement.class,
			Object.class
		}
	)
})
public class MyBatisDefaultColumnInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return null;
	}

	@Override
	public Object plugin(Object target) {
		return null;
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
}