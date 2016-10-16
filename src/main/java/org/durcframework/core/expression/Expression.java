package org.durcframework.core.expression;


/**
 * 查询条件接口
 * 
 * @author thc 2011-10-28
 */
public interface Expression {
	void addToQuery(ExpressionQuery query);
}
