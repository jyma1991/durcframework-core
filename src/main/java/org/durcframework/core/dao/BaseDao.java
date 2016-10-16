package org.durcframework.core.dao;

import java.util.List;

import org.durcframework.core.expression.ExpressionQuery;

/**
 * DAO类,其它DAO类都要继承该类
 * @author hc.tang
 * 
 * @param <Entity>
 */
public interface BaseDao<Entity> {
	// 根据对象查询,可以传主键值,也可以传整个对象
	Entity get(Object id);
	// 条件查询
	List<Entity> find(ExpressionQuery query);
	// 查询总记录数
	Integer findTotalCount(ExpressionQuery query);
	// 保存
	void save(Entity entity);
	// 修改
	void update(Entity entity);
	// 删除
	void del(Entity entity);
}