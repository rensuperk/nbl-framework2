/*
 * Copyright (c) 2001-2016 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 * <p>DubboServiceConfig.java</p>
 */
package cn.bidlink.nbl.framework.dubbo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;

/**
 * TODO jiamingliu 类描述.
 *
 * @version : Ver 1.0
 * @author	: <a href="mailto:jiamingliu@ebnew.com">jiamingliu</a>
 * @date	: 2016年6月20日 上午8:51:53 
 */
@Configuration
@Conditional(DubboConfigCondition.class)
@ImportResource("classpath:spring/applicationContext-dubbo.xml")
public class DubboConfig {
	
	protected Logger logger = LoggerFactory.getLogger(DubboConfig.class);
	
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Bean
	public AnnotationBean annotationBean(){
		AnnotationBean bean = new AnnotationBean();
		bean.setPackage("cn.bidlink.nbl");
		return bean;
	}
	
	@Bean
	public ProtocolConfig protocolConfig(){
		ProtocolConfig config = new ProtocolConfig();
		config.setPort(-1);
		return config;
	}
}