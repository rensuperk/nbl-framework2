package cn.bidlink.nbl.framework.dao.saas.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.bidlink.framework.core.support.jta.JTAInterceptor;
import cn.bidlink.framework.dao.datasource.DataSourceLoader;
import cn.bidlink.framework.dao.ibatis.MyBatisBaseDao;
import cn.bidlink.framework.dao.ibatis.impl.MyBatisBaseDaoImpl;
import cn.bidlink.framework.dao.ibatis.interceptor.DaoInterceptor;
import cn.bidlink.nbl.framework.dao.saas.DaoDefaultValueInterceptor;
import cn.bidlink.nbl.framework.dao.saas.DsRouteIsolationFilter;

@Configuration
@Conditional(SaasDaoConfigCondition.class)
public class SaasDaoConfig implements EnvironmentAware {
	
	private String excludePaths;
	
	@Override
	public void setEnvironment(Environment env) {
		this.excludePaths = env.getProperty("ds.route.filter.exclude");
	}

	@Bean(name="dataRouteFilter")
	DsRouteIsolationFilter dataIsolationFilter(){
		DsRouteIsolationFilter filter = new DsRouteIsolationFilter();
		filter.setExcludePaths(excludePaths.split(","));
		return filter;
	}
	
	@Bean(name="dataSourceLoader")
	DataSourceLoader dataSourceLoader(){
		DataSourceLoader loader = new DataSourceLoader();
		Set<String> parttens = new HashSet<>(1);
		parttens.add("classpath*:cn/bidlink/nbl/**/model/**/mapper/**/*.xml");
		loader.setScnParttens(parttens);
		return loader;
	}
	
	@Bean(name="jtaInterceptor")
	JTAInterceptor jtaInterceptor(){
		return new JTAInterceptor();
	}
	
	@Bean(name="daoInterceptor")
	DaoInterceptor daoInterceptor(){
		return new DaoInterceptor();
	}
	
	@Bean
	DaoDefaultValueInterceptor daoDefaultValueInterceptor(){
		return new DaoDefaultValueInterceptor();
	}
	
	@Bean(name="myBatisBaseDao")
	MyBatisBaseDao<?, ?> myBatisBaseDao(){
		return new MyBatisBaseDaoImpl<>();
	}
}