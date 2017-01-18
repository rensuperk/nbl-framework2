package cn.bidlink.nbl.framework.canary;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.bidlink.nbl.framework.canary.model.NoticeIndex;
import cn.bidlink.nbl.framework.canary.model.NoticeQueryCondition;
import cn.bidlink.nbl.framework.canary.service.NoticeIndexService;
import cn.bidlink.nbl.framework.core.config.BootstrapConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BootstrapConfig.class })
public class NoticeIndexServiceTest {
	
	@Autowired
	private NoticeIndexService noticeIndexService;
	
	@Test
	public void testQuery(){
		NoticeQueryCondition condition = new NoticeQueryCondition();
		condition.setKeywords("ct");
		List<NoticeIndex> notices = noticeIndexService.query(condition);
		for(NoticeIndex notice : notices){
			System.out.println(notice.getTitle());
		}
	}
}