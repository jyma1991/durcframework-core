package org.durcframework.core;

/**
 * 负责查询的实体类的默认实现,按页码分页
 * 
 * @author hc.tang 2013年11月14日
 * 
 */
public class SearchEntity implements SearchSupport {

	/** 第一页 */
	private int pageIndex = 1;
	/** 每页10条记录 */
	private int pageSize = 10;

	private String sortname;
	private String sortorder;


	@Override
	public int getLimit() {
		return pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		if (pageIndex <= 0) {
			pageIndex = 1;
		}
		this.pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize) {
		if (pageSize <= 0) {
			pageSize = 10;
		}
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	@Override
	public String getSortname() {
		return sortname;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	@Override
	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public int getStart() {
		return (int) ((pageIndex - 1) * pageSize);
	}
}
