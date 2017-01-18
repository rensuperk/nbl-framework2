/*
 * Copyright (c) 2001-2016 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 * <p>DubboCondition.java</p>
 */
package cn.bidlink.nbl.framework.dubbo.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * TODO jiamingliu 类描述.
 *
 * @version : Ver 1.0
 * @author	: <a href="mailto:jiamingliu@ebnew.com">jiamingliu</a>
 * @date	: 2016年6月20日 上午9:44:14 
 */
public class DubboConfigCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return Boolean.parseBoolean(context.getEnvironment().getProperty("dubbo.open", "true"));
	}
}