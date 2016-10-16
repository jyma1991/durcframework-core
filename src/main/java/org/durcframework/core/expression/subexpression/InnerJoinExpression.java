package org.durcframework.core.expression.subexpression;

import org.durcframework.core.expression.SqlContent;

/**
 * 内连接条件
 * 
 * @author thc 2011-10-28
 */
public class InnerJoinExpression extends AbstractJoinExpression {

	/**
	 * 内连接
	 * @param secondTableName 第二张表名
	 * @param secondTableTableAlias 第二张表别名
	 * @param firstTableColumn 第一张表管理字段
	 * @param secondTableColumn 第二张管理表字段
	 */
	public InnerJoinExpression(String secondTableName,
			String secondTableTableAlias, String firstTableColumn,
			String secondTableColumn) {
		super(secondTableName, secondTableTableAlias, firstTableColumn,
				secondTableColumn);
	}

	@Override
	protected String getJoinType() {
		return SqlContent.INNER_JOIN;
	}

}
