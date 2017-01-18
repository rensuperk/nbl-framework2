package cn.bidlink.nbl.framework.mybatis;

import java.util.Map;
import java.util.Properties;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import tk.mybatis.mapper.entity.Example;

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
public class MyBatisUserAuthInterceptor implements Interceptor {

	static final int MAPPED_STATEMENT_INDEX = 0;
	static final int PARAMETER_INDEX = 1;
	static final int ROWBOUNDS_INDEX = 2;
	static final int RESULT_HANDLER_INDEX = 3;
	
	private void handleParameter(Object parameter, String name, Object value){
		if(parameter instanceof Example){
			
		}else if(parameter instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) parameter;
			map.put(name, value);
		}else{
			try {
				FieldUtils.writeDeclaredField(parameter, name, value, true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object [] args = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement)args[MAPPED_STATEMENT_INDEX];
	 	Object parameter = args[PARAMETER_INDEX];
	 	BoundSql boundSql = mappedStatement.getBoundSql(parameter);
	 	for(ParameterMapping mapping : boundSql.getParameterMappings()){
	 		switch(mapping.getProperty()){
	 		case "tenantId":
	 			handleParameter(parameter, mapping.getProperty(), "test");
	 			break;
	 		}
	 		
	 	}
//	 	String sql = boundSql.getSql().trim();
	 	switch(invocation.getMethod().getName()){
	 	case "query":
	 		mappedStatement.getParameterMap().getParameterMappings();
	 	case "update":
	 		mappedStatement.getParameterMap().getParameterMappings();
	 	}
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