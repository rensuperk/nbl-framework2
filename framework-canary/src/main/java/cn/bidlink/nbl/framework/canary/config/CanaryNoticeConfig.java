package cn.bidlink.nbl.framework.canary.config;

import org.beanguo.canary.canary_search_client.BeanClient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import cn.bidlink.nbl.framework.canary.service.NoticeIndexService;
import cn.bidlink.nbl.framework.canary.service.impl.NoticeIndexServiceImpl;

@Configuration
public class CanaryNoticeConfig implements EnvironmentAware {
	
	private String ip;
	private Integer port;
	private String appKey;

	@Override
	public void setEnvironment(Environment env) {
		this.ip = env.getProperty("canary.ip");
		this.port = env.getProperty("canary.port", Integer.class);
		this.appKey = env.getProperty("canary.notice.app.key");
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	BeanClient noticeSearchClient(){
		return new BeanClient(ip, port, appKey);
	}
	
	/**
	 * 必联老索引searchbu的实现
	 * */
	@Bean
	NoticeIndexService noticeIndexService(){
		NoticeIndexServiceImpl noticeIndexService = new NoticeIndexServiceImpl();
		noticeIndexService.setNoticeSearchClient(noticeSearchClient());
		return noticeIndexService;
	}
}