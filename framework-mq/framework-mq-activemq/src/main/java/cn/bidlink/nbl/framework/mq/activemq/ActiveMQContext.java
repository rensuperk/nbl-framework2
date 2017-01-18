package cn.bidlink.nbl.framework.mq.activemq;

import javax.jms.Session;

public class ActiveMQContext {
	
	private static final ThreadLocal<Session> SESSIONS = new ThreadLocal<>();
	
	public static final Session getCurrentSession(){
		return SESSIONS.get();
	}
	public static final boolean hasCurrentSession(){
		return SESSIONS.get() != null;
	}
	public static final void setCurrentSession(Session session){
		SESSIONS.set(session);
	}
}