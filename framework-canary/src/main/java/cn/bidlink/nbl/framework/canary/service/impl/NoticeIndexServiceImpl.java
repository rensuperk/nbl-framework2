package cn.bidlink.nbl.framework.canary.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.beanguo.canary.canary_search_client.BeanClient;
import org.beanguo.canary.canary_search_client.Param;
import org.beanguo.canary.core.HttpResp;
import org.beanguo.canary.core.exception.CanarySearchException;

import cn.bidlink.nbl.framework.canary.model.NoticeIndex;
import cn.bidlink.nbl.framework.canary.model.NoticeQueryCondition;
import cn.bidlink.nbl.framework.canary.service.NoticeIndexService;
import cn.bidlink.nbl.framework.canary.util.CanaryQueryUtil;
import cn.bidlink.nbl.framework.core.util.StringUtil;

public class NoticeIndexServiceImpl implements NoticeIndexService {
	
	private BeanClient noticeSearchClient;
	
	public void setNoticeSearchClient(BeanClient noticeSearchClient) {
		this.noticeSearchClient = noticeSearchClient;
	}
	
	@Override
	public List<NoticeIndex> query(NoticeQueryCondition condition) {
		Param param = new Param();
		try {
			//关键词之间的空格为OR的规则
			param.setQ(CanaryQueryUtil.parseArray("fullText", condition.getKeywords().split(" "), "OR"));
			List<String> fqs = new ArrayList<>();
			//非测试数据
			fqs.add(CanaryQueryUtil.parse("is_test", 0));
			//信息类型
			if(StringUtil.isNotEmpty(condition.getInfoTypes())){
				fqs.add(CanaryQueryUtil.parseArray("infoClassCodes", condition.getInfoTypes().split(","), "OR"));
			}
			//招标类型
			if(StringUtil.isNotEmpty(condition.getPurchaseType())){
				fqs.add(CanaryQueryUtil.parse("bidModel", condition.getPurchaseType()));
			}
			//资金来源
			if(StringUtil.isNotEmpty(condition.getFundSourceCodes())){
				fqs.add(CanaryQueryUtil.parseArray("fundSourceCodes", condition.getFundSourceCodes().split(","), "OR"));
			}
			//行业
			if(StringUtil.isNotEmpty(condition.getIndustryCodes())){
				fqs.add(CanaryQueryUtil.parseArray("normIndustry", condition.getIndustryCodes().split(","), "OR"));
			}
			//地区
			if(StringUtil.isNotEmpty(condition.getAreaCode())){
				fqs.add(CanaryQueryUtil.parseArray("zone", condition.getAreaCode().split(","), "OR"));
			}
			//时间段
			if(condition.getCrTimeBegin() != null && condition.getCrTimeEnd() != null){
				fqs.add(CanaryQueryUtil.parseRange("crTime", condition.getCrTimeBegin(), condition.getCrTimeEnd()));
			}
			param.setFq(fqs);
			HttpResp<NoticeIndex> resp = noticeSearchClient.searchPost(param, NoticeIndex.class);
			return resp.getResult();
		} catch (CanarySearchException e) {
			e.printStackTrace();
		}
		return null;
	}
}