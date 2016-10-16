package org.durcframework.core.expression.subexpression;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.SqlContent;

/**
 * 拼接SQL语句
 * 
 * @author hc.tang
 * 
 */
public class SqlExpression implements Expression {

	private String joint = SqlContent.AND;
	private String sql;

	public SqlExpression(String sql) {
		this.sql = sql;
	}
	
	public SqlExpression(String joint,String sql) {
		this.joint = joint;
		this.sql = sql;
	}

	@Override
	public void addToQuery(ExpressionQuery query) {
		query.addSqlExpression(this);
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getJoint() {
		return joint;
	}

	public void setJoint(String joint) {
		this.joint = joint;
	}
	
}
