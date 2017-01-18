/*
 * Copyright (c) 2001-2016 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 * <p>AuthConfig.java</p>
 */
package cn.bidlink.nbl.framework.user.config;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.bidlink.nbl.framework.auth.cookie.CustomCookie;
import cn.bidlink.nbl.framework.auth.handler.AccessDecisionHandler;
import cn.bidlink.nbl.framework.auth.handler.AuthcCheckHandler;
import cn.bidlink.nbl.framework.auth.handler.AuthzAssignHandler;
import cn.bidlink.nbl.framework.auth.handler.AuthzCheckHandler;
import cn.bidlink.nbl.framework.auth.handler.CasLoginHandler;
import cn.bidlink.nbl.framework.auth.handler.CasLogoutHandler;
import cn.bidlink.nbl.framework.auth.handler.DataAuthConditionHandler;
import cn.bidlink.nbl.framework.auth.handler.ThreadLocalSetHandler;

/**
 * TODO jiamingliu 类描述.
 *
 * @version : Ver 1.0
 * @author	: <a href="mailto:jiamingliu@ebnew.com">jiamingliu</a>
 * @date	: 2016年6月23日 上午11:25:35 
 */
@Configuration
@Conditional(AuthorityConfigCondition.class)
public class AuthorityConfig implements EnvironmentAware {
	
	protected Logger logger = LoggerFactory.getLogger(AuthorityConfig.class);
	
	private String casServerUrl;
	private String casLoginUrl;
	private String casLogoutUrl;
	private String loginPageUrl;
	
	private String redisHost;
	private int redisPort;
	private String redisPassword;
	
	private String casLoginFilterUrl;
	private String casLogoutFilterUrl;

	private String filterChain;

	@Override
	public void setEnvironment(Environment env) {
		this.casServerUrl = env.getProperty("auth.cas.server.url");
		this.casLoginUrl = env.getProperty("auth.cas.login.url");
		this.casLogoutUrl = env.getProperty("auth.cas.logout.url");
		this.loginPageUrl = env.getProperty("auth.common.login.page.url");
		this.redisHost = env.getProperty("auth.redis.host");
		this.redisPort = env.getProperty("auth.redis.port", Integer.class);
		this.redisPassword= env.getProperty("auth.redis.password");
		this.casLoginFilterUrl = env.getProperty("auth.cas.login.filter.url");
		this.casLogoutFilterUrl = env.getProperty("auth.cas.logout.filter.url");
		this.filterChain = env.getProperty("auth.filter.chain");
		if ( isBlank( this.filterChain ) ){
			this.filterChain = "accessDecisionHandler,authcCheckHandler,authzCheckHandler,dataAuthConditionHandler,threadLocalSetHandler";
		}
	}
	
	@Bean(name="authzAssignHandler")
	public AuthzAssignHandler authzAssignHandler(){
		AuthzAssignHandler handler = new AuthzAssignHandler();
		handler.setCasServerUrlPrefix(casServerUrl);
		handler.setCasService(casLoginUrl);
		return handler;
	}
	
	@Bean(name="authcCheckHandler")
	public AuthcCheckHandler authcCheckHandler() throws UnsupportedEncodingException{
		AuthcCheckHandler handler = new AuthcCheckHandler();
		String loginUrl = casServerUrl + "/login"
				+ "?service=" + casLoginUrl
				+ "&realService=" + casLogoutUrl
				+ "&loginUrl=" + URLEncoder.encode(loginPageUrl, "UTF-8");
		handler.setLoginUrl(loginUrl);
		return handler;
	}
	
	@Bean(name="accessDecisionHandler")
	public AccessDecisionHandler accessDecisionHandler(){
		return new AccessDecisionHandler();
	}
	
	@Bean(name="authzCheckHandler")
	public AuthzCheckHandler authzCheckHandler(){
		return new AuthzCheckHandler();
	}
	
	@Bean(name="casLoginHandler")
	public CasLoginHandler casLoginHandler(){
		return new CasLoginHandler();
	}
	
	@Bean(name="casLogoutHandler")
	public CasLogoutHandler casLogoutHandler(){
		return new CasLogoutHandler();
	}
	
	@Bean(name="dataAuthConditionHandler")
	public DataAuthConditionHandler dataAuthConditionHandler(){
		return new DataAuthConditionHandler();
	}
	
	@Bean(name="threadLocalSetHandler")
	public ThreadLocalSetHandler threadLocalSetHandler(){
		return new ThreadLocalSetHandler();
	}
	
	@Bean(name="systemShiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(){
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager());
		//此处要使用有序的map,要不有可能直接执行/**的过滤
		Map<String, String> filterMap = new LinkedHashMap<>();
		filterMap.put(casLoginFilterUrl, "casLoginHandler");
		filterMap.put(casLogoutFilterUrl, "casLogoutHandler");
		//filterMap.put("/**", "accessDecisionHandler,authcCheckHandler,authzCheckHandler,dataAuthConditionHandler,threadLocalSetHandler");
		filterMap.put("/**", this.filterChain );
		bean.setFilterChainDefinitionMap(filterMap);
		return bean;
	}

	@Bean(name="securityManager")
	public DefaultWebSecurityManager securityManager(){
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setRealm(authzAssignHandler());
		manager.setSubjectFactory(casSubjectFactory());
		manager.setCacheManager(cacheManager());
		manager.setSessionManager(sessionManager());
		return manager;
	}
	
	@Bean(name="simpleCustomCookie")
	public CustomCookie customCookie(){
		return new CustomCookie();
	}
	
	@Bean(name="shiroRedisManager")
	public RedisManager redisManager(){
		RedisManager manager = new RedisManager();
		manager.setHost(redisHost);
		manager.setPort(redisPort);
		manager.setPassword(redisPassword);
		manager.setExpire(1800);
		return manager;
	}
	
	@Bean(name="shiroRedisSessionDAO")
	public RedisSessionDAO redisSessionDao(){
		RedisSessionDAO dao = new RedisSessionDAO();
		dao.setRedisManager(redisManager());
		return dao;
	}
	
	@Bean(name="shiroSessionManager")
	public DefaultWebSessionManager sessionManager(){
		DefaultWebSessionManager manager = new DefaultWebSessionManager();
		manager.setSessionDAO(redisSessionDao());
		return manager;
	}
	
	@Bean(name="shiroCacheManager")
	public RedisCacheManager cacheManager(){
		RedisCacheManager manager = new RedisCacheManager();
		manager.setRedisManager(redisManager());
		return manager;
	}
	
	@Bean(name="casSubjectFactory")
	public CasSubjectFactory casSubjectFactory(){
		return new CasSubjectFactory();
	}
	
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean(){
		MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
		bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		bean.setArguments(new Object[]{ securityManager() });
		return bean;
	}
	
	@Bean(name="lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}

	private boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}