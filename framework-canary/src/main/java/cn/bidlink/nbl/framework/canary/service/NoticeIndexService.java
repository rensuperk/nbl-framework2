package cn.bidlink.nbl.framework.canary.service;

import java.util.List;

import cn.bidlink.nbl.framework.canary.model.NoticeIndex;
import cn.bidlink.nbl.framework.canary.model.NoticeQueryCondition;

/**
 * 公告索引服务
 * */
public interface NoticeIndexService {
	
	/**
	 * 查询
	 * */
	public List<NoticeIndex> query(NoticeQueryCondition condition);

}