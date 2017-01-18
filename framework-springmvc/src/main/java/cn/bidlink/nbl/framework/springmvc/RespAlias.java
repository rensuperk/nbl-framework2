/*
 * Copyright (c) 2001-2016 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 * <p>RespAlias.java</p>
 */
package cn.bidlink.nbl.framework.springmvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO jiamingliu 类描述.
 *
 * @version : Ver 1.0
 * @author	: <a href="mailto:jiamingliu@ebnew.com">jiamingliu</a>
 * @date	: 2016年6月20日 下午5:13:56 
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RespAlias {
	
	String value();

}