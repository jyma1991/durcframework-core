package org.durcframework.core.expression.subexpression;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.JoinExpression;

public class DefaultJoinExpression implements JoinExpression,Expression {

	private String joinSql;
	
	/**
	 * 自定义连接语句
	 * @param joinSql inner join table1 t1 on t.xx = t1.xx
	 */
	public DefaultJoinExpression(String joinSql){
		this.joinSql = joinSql;
	}
	
	@Override
	public String getExprString() {
		return joinSql;
	}

	@Override
	public void addToQuery(ExpressionQuery query) {
		query.addJoinExpression(this);
	}

}
