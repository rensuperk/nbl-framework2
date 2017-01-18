package cn.bidlink.nbl.framework.core.util;

import java.util.List;

public class Page<T> {

	private Integer pageNumber;
	private Integer pageSize;
	private Long count;
	private List<T> result;
	
	private Integer offset;
	private Integer limit;
	
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public Integer getOffset() {
		return offset;
	}
	public Integer getLimit() {
		return limit;
	}
}