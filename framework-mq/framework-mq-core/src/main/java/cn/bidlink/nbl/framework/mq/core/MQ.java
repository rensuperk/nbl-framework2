package cn.bidlink.nbl.framework.mq.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MQ {

	DestinationType destinationType() default DestinationType.QUEUE;
	String destinationName();
}