package cn.bidlink.nbl.framework.mybatis.config;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;
import cn.bidlink.nbl.framework.mybatis.MyBatisDefaultColumnInterceptor;
import cn.bidlink.nbl.framework.mybatis.MyBatisUserAuthInterceptor;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;

@Configuration
public class MyBatisConfig implements EnvironmentAware, ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	private String scanPackage;
	private String dbType;
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	private String mapperLocations;
	private String typeAliasesPackage;
	
	@Override
	public void setEnvironment(Environment env) {
		this.scanPackage = env.getProperty("mybatis.scan.package");
		this.dbType = env.getProperty("mybatis.db.type");
		this.dbUrl = env.getProperty("mybatis.db.url");
		this.dbUsername = env.getProperty("mybatis.db.username");
		this.dbPassword = env.getProperty("mybatis.db.password");
		//"classpath*:cn/bidlink/**/mapper/*.xml"
		this.mapperLocations = env.getProperty("mybatis.mapper.locations");
		//"classpath:cn.bidlink.**.model"
//		this.typeAliasesPackage = env.getProperty("mybatis.type.aliases.package");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean(initMethod="init", destroyMethod="close")
	DataSource dataSource(){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDbType(dbType);
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);
		dataSource.setMaxActive(20);
		dataSource.setInitialSize(1);
		dataSource.setMaxWait(60000);
		dataSource.setMinIdle(1);
		dataSource.setTimeBetweenEvictionRunsMillis(3000);
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setValidationQuery("SELECT 'x'");
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestOnReturn(false);
		//打开PSCache，并且指定每个连接上PSCache的大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		//是否打开removeAbandoned功能，对于建立时间超过removeAbandonedTimeout的连接强制关闭
		dataSource.setRemoveAbandoned(false);
		/**
		 * 打开removeAbandoned功能
		 * */
		//多少秒删除连接，秒为单位，指定连接建立多长时间就需要被强制关闭，如果removeAbandoned为false，这个设置项不再起作用
		dataSource.setRemoveAbandonedTimeout(7200);
		//关闭abanded连接时输出错误日志，指定发生removeAbandoned的时候，是否记录当前线程的堆栈信息到日志中
		dataSource.setLogAbandoned(true);
		//配置监控统计拦截的filters
		try {
			dataSource.setFilters("stat,log4j");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<Filter> filters = new ArrayList<>();
		StatFilter statFilter = new StatFilter();
		statFilter.setSlowSqlMillis(10000);
		statFilter.setLogSlowSql(true);
		statFilter.setMergeSql(true);
		filters.add(statFilter);
		
		WallFilter wallFilter = new WallFilter();
		filters.add(wallFilter);
		
		Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
		slf4jLogFilter.setResultSetLogEnabled(true);
		slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
		filters.add(slf4jLogFilter);
		dataSource.setProxyFilters(filters);
		return dataSource;
	}
	
	@Bean
	SqlSessionFactory sqlSessionFactory(){
		DataSource dataSource = applicationContext.getBean(DataSource.class);
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		try {
			factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		factory.setPlugins(new Interceptor[]{
			new MyBatisUserAuthInterceptor()
		});
		
//		factory.setTypeAliasesPackage(typeAliasesPackage);
//		MybatisDaoLimitInterceptor limitor = new MybatisDaoLimitInterceptor();
//		factory.setPlugins(new Interceptor[]{ limitor });
		try {
			return factory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Bean
	MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer scanner = new MapperScannerConfigurer();
		scanner.setBasePackage(scanPackage);
		scanner.setMarkerInterface(Mapper.class);
		return scanner;
	}
	
//	@Bean
//	MapperScannerConfigurer mapperScannerConfigurer(){
//		MapperScannerConfigurer config = new MapperScannerConfigurer();
//		config.setSqlSessionFactoryBeanName("sqlSessionFactory");
//		config.setAnnotationClass(Dao.class);
//		config.setBasePackage(scanPackage);
//		return config;
//	}
}