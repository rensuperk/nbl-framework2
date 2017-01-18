package cn.bidlink.nbl.framework.canary.model;

import java.io.Serializable;
import java.util.Date;

import org.beanguo.canary.core.annotation.Convert;

/**
 * 公告索引
 * */
public class NoticeIndex implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	@Convert("docAbstract")
	private String content;
	// 招标项目编号
	@Convert("bidcode_t")
	private String projectNumber;
	// 行业
	@Convert("normIndustry")
	private String[] industryCodes;
	@Convert("industrys")
	private String[] industryNames;
	// 信息类型
	@Convert("infoClassCodes")
	private String[] infoTypeCodes;
	@Convert("infoClasses")
	private String[] infoTypeNames;
	//资金来源
	private String[] fundSourceCodes;
	private String[] fundSourceNames;
	// 地区
	@Convert("zone")
	private String areaCode;
	@Convert("zonename")
	private String areaName;
	// 采购类型
	@Convert("bidModel")
	private String purchaseType;
	// 开标时间
	private Date bidOpenTime;
	// 招标机构
	@Convert("orgName")
	private String tenderName;
	// 关键词
	@Convert("indexwords")
	private String keywords;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getProjectNumber() {
		return projectNumber;
	}
	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}
	public String[] getIndustryCodes() {
		return industryCodes;
	}
	public void setIndustryCodes(String[] industryCodes) {
		this.industryCodes = industryCodes;
	}
	public String[] getIndustryNames() {
		return industryNames;
	}
	public void setIndustryNames(String[] industryNames) {
		this.industryNames = industryNames;
	}
	public String[] getInfoTypeCodes() {
		return infoTypeCodes;
	}
	public void setInfoTypeCodes(String[] infoTypeCodes) {
		this.infoTypeCodes = infoTypeCodes;
	}
	public String[] getInfoTypeNames() {
		return infoTypeNames;
	}
	public void setInfoTypeNames(String[] infoTypeNames) {
		this.infoTypeNames = infoTypeNames;
	}
	public String[] getFundSourceCodes() {
		return fundSourceCodes;
	}
	public void setFundSourceCodes(String[] fundSourceCodes) {
		this.fundSourceCodes = fundSourceCodes;
	}
	public String[] getFundSourceNames() {
		return fundSourceNames;
	}
	public void setFundSourceNames(String[] fundSourceNames) {
		this.fundSourceNames = fundSourceNames;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPurchaseType() {
		return purchaseType;
	}
	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}
	public Date getBidOpenTime() {
		return bidOpenTime;
	}
	public void setBidOpenTime(Date bidOpenTime) {
		this.bidOpenTime = bidOpenTime;
	}
	public String getTenderName() {
		return tenderName;
	}
	public void setTenderName(String tenderName) {
		this.tenderName = tenderName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}