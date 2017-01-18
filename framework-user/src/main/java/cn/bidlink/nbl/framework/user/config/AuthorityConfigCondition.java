/*
 * Copyright (c) 2001-2016 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 * <p>AuthorityCondition.java</p>
 */
package cn.bidlink.nbl.framework.user.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * TODO jiamingliu 类描述.
 *
 * @version : Ver 1.0
 * @author	: <a href="mailto:jiamingliu@ebnew.com">jiamingliu</a>
 * @date	: 2016年6月23日 下午1:25:26 
 */
public class AuthorityConfigCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		return Boolean.parseBoolean(context.getEnvironment().getProperty("auth.open", "false"));
	}
}