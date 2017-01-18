package cn.bidlink.nbl.framework.mq.activemq;

import javax.jms.Session;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ActiveMQTransationalInterceptor {
	
	@Around(value = "@Annotation(cn.bidlink.nbl.framework.mq.activemq.ActiveMQTransactional)", argNames = "pjp")
	public Object doIntercept(ProceedingJoinPoint pjp) {
//		Object target = pjp.getTarget();
		try {
			Object obj = pjp.proceed();
			if(ActiveMQContext.hasCurrentSession()){
				Session session = ActiveMQContext.getCurrentSession();
				session.commit();
			}
			return obj;
		} catch (Throwable e) {
		    throw new RuntimeException(e);
		} finally {
//			DynamicDataSource.CONTEXT_HOLDER.remove();
		}
	}
}