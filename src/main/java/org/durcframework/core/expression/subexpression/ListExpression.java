package org.durcframework.core.expression.subexpression;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.ExpressionQuery;
import org.durcframework.core.expression.SqlContent;
import org.durcframework.core.expression.ValueConvert;


/**
 * list或数组查询雕件
 * @author thc
 * 2011-10-28
 */
public class ListExpression implements Expression {

	private String column = "";
	private String equal = SqlContent.IN;
	private Collection<?> value = Collections.EMPTY_LIST;
	private String joint = SqlContent.AND;
	
	public ListExpression(String column, Collection<?> value) {
		this.column = column;
		this.value = value;
	}
	
	public ListExpression(String column, Collection<?> value,ValueConvert valueConvert) {
		this.column = column;
		Collection<Object> newSet = new HashSet<Object>();
		for (Object obj : value) {
			newSet.add(valueConvert.convert(obj));
		}
		this.value = newSet;
	}
	
	public ListExpression(String column, Object[] value) {
		this.column = column;
		this.value = Arrays.asList(value);
	}
	
	public ListExpression(String column, String equal, Object[] value) {
		this(column, value);
		this.equal = equal;
	}
	public ListExpression(String joint,String column, String equal, Object[] value) {
		this(column,equal, value);
		this.joint = joint;
	}

	public ListExpression(String column, String equal, Collection<?> value) {
		this(column, value);
		this.equal = equal;
	}

	public ListExpression(String joint, String column, String equal,
			Collection<?> value) {
		this(column, equal, value);
		this.joint = joint;
	}

	@Override
	public void addToQuery(ExpressionQuery query) {
		query.addListExpression(this);
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getEqual() {
		return equal;
	}

	public void setEqual(String equal) {
		this.equal = equal;
	}

	public Collection<?> getValue() {
		return value;
	}

	public void setValue(Collection<?> value) {
		this.value = value;
	}

	public String getJoint() {
		return joint;
	}

	public void setJoint(String joint) {
		this.joint = joint;
	}

}
