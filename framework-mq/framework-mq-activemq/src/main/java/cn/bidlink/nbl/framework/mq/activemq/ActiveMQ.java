package cn.bidlink.nbl.framework.mq.activemq;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.activemq.store.kahadb.data.KahaDestination.DestinationType;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ActiveMQProducer {
	
	DestinationType destinationType() default DestinationType.QUEUE;
	String destinationName();

}