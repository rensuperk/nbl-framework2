package cn.bidlink.nbl.framework.canary.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告搜索条件
 * */
public class NoticeQueryCondition implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 搜索关键词，多个关键词之间用空格分开
	 */
	private String keywords;
	/**
	 * 信息类型
	 */
	private String infoTypes;
	/**
	 * 招标类型
	 */
	private String purchaseType;
	/**
	 * 资金来源
	 */
	private String fundSourceCodes;
	/**
	 * 地区
	 */
	private String areaCode;
	/**
	 * 行业
	 */
	private String industryCodes;
	/**
	 * 自定义起止时间:开始时间
	 */
	private Date crTimeBegin;
	/**
	 * 自定义起止时间:结束时间
	 */
	private Date crTimeEnd;
	/**
	 * 排序字段
	 */
	private String orderBy;
	/**
	 * 排序方式
	 */
	private String orderType;
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getInfoTypes() {
		return infoTypes;
	}
	public void setInfoTypes(String infoTypes) {
		this.infoTypes = infoTypes;
	}
	public String getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}
	public String getFundSourceCodes() {
		return fundSourceCodes;
	}
	public void setFundSourceCodes(String fundSourceCodes) {
		this.fundSourceCodes = fundSourceCodes;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getIndustryCodes() {
		return industryCodes;
	}
	public void setIndustryCodes(String industryCodes) {
		this.industryCodes = industryCodes;
	}
	public Date getCrTimeBegin() {
		return crTimeBegin;
	}
	public void setCrTimeBegin(Date crTimeBegin) {
		this.crTimeBegin = crTimeBegin;
	}
	public Date getCrTimeEnd() {
		return crTimeEnd;
	}
	public void setCrTimeEnd(Date crTimeEnd) {
		this.crTimeEnd = crTimeEnd;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}