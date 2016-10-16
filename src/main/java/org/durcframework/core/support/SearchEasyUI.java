package org.durcframework.core.support;

import java.util.Map;

import org.durcframework.core.SearchEntity;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

/**
 * jquery easyUI的datagrid查询类
 */
public class SearchEasyUI extends SearchEntity {

	// 当前第几页
	private int page = 1;
	// 每页记录数
	private int rows = 20;

	private String sort;
	private String order;

	public SearchEasyUI() {
		this.setPageIndex(page);
		this.setPageSize(rows);
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
		this.setPageIndex(this.page);
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.setPageSize(this.rows);
	}

	public void setSort(String sort) {
		this.sort = sort;
		this.setSortname(this.sort);
	}

	public void setOrder(String order) {
		this.order = order;
		this.setSortorder(this.order);
	}

	/**
	 * 子类可覆盖此方法
	 * 
	 * <pre>
	 * <code>
	 * private static Map<String, String> sortMap;
	
	static {
		sortMap = new HashMap<String, String>(2);
		// 排序字段映射,java类中的字段对应成数据库中的字段
		sortMap.put("addTime", "add_time");
		sortMap.put("lastLoginDate", "last_login_date");
	}
	
	protected Map<String, String> getSortMap() {
		return sortMap;
	}
	 * </code>
	 * </pre>
	 * 
	 * @return
	 */
	protected Map<String, String> getSortMap() {
		return null;
	}

	@Override
	public String getSortname() {
		return buildSortname(super.getSortname());
	}

	// 构建真实的排序名称
	// 子类覆盖getSortMap()方法,并返回java字段与数据库中的字段名隐射关系
	private String buildSortname(String defSortname) {
		Map<String, String> sortMap = this.getSortMap();

		if (sortMap != null) {
			String sortname = sortMap.get(defSortname);
			if (sortname != null) {
				return sortname;
			}
		}

		return defSortname;
	}

}
