package org.durcframework.core.service;

import java.util.Collections;
import java.util.List;

import org.durcframework.core.dao.BaseDao;
import org.durcframework.core.expression.ExpressionQuery;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 负责查询的Service
 * 
 * @author hc.tang
 * 
 * @param <Entity>
 * @param <Dao>
 */
public abstract class SearchService<Entity, Dao extends BaseDao<Entity>> {
	
	private Dao dao;

	public Dao getDao() {
		return dao;
	}

	@Autowired
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	/**
	 * 通过对象获取记录,可以传主键值,也可以传整个对象
	 * @param id 如:get(21)或get(student)
	 * @return 返回实体对象
	 */
	public Entity get(Object id) {
		return dao.get(id);
	}

	/**
	 * 带入条件查询,返回结果集
	 * @param query
	 * @return 返回结果一定不为null,如果没有数据则返回一个空List
	 */
	public List<Entity> find(ExpressionQuery query) {
		List<Entity> list = dao.find(query);
		if(list == null) {
			list = Collections.emptyList();
		}
		return list;
	}

	/**
	 * 带入条件查询总数
	 * @param query
	 * @return 返回的结果一定大于等于0
	 */
	public int findTotalCount(ExpressionQuery query) {
		Integer total = dao.findTotalCount(query);
		return total == null ? 0 : total;
	}

}
