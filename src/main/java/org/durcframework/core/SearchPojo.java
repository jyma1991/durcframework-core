package org.durcframework.core;

/**
 * 使用start,limit可实现不规则分页. 如MYSQL:SELECT * FROM table LIMIT 3,4
 * @author hc.tang
 *
 */
public class SearchPojo implements SearchSupport {

	private int start;
	private int limit = 10;

	private String sortname;
	private String sortorder;

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setSortname(String sortname) {
		this.sortname = sortname;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	@Override
	public String getSortname() {
		return sortname;
	}

	@Override
	public String getSortorder() {
		return sortorder;
	}

}
