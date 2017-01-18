package cn.bidlink.nbl.framework.mybatis;

import java.util.Properties;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.RowBounds;

public abstract class MyBatisSqlInterceptor implements Interceptor {

	static final int MAPPED_STATEMENT_INDEX = 0;
	static final int PARAMETER_INDEX = 1;
	static final int ROWBOUNDS_INDEX = 2;
	static final int RESULT_HANDLER_INDEX = 3;
	
	public abstract String doSelect(Select select, RowBounds rowBounds);
	public abstract String doInsert(Insert insert);
	public abstract String doUpdate(Update update);
	public abstract String doDelete(Delete delete);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object [] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement)args[MAPPED_STATEMENT_INDEX];
	 	Object parameter = args[PARAMETER_INDEX];
	 	BoundSql boundSql = mappedStatement.getBoundSql(parameter);
	 	String sql = boundSql.getSql().trim();
	 	Statement statement = CCJSqlParserUtil.parse(sql);
	 	
	 	if(statement instanceof Select){
	 		RowBounds rowBounds = (RowBounds) args[ROWBOUNDS_INDEX];
	 		sql = doSelect((Select)statement, rowBounds);
	 	}else if(statement instanceof Insert){
	 		sql = doInsert((Insert)statement);
	 	}else if(statement instanceof Update){
	 		sql = doUpdate((Update)statement);
	 	}else if(statement instanceof Delete){
	 		sql = doDelete((Delete)statement);
	 	}
 		
 		SqlSource sqlSource = new StaticSqlSource(mappedStatement.getConfiguration(), sql);
 		args[MAPPED_STATEMENT_INDEX] = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType()).build();
 		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
}