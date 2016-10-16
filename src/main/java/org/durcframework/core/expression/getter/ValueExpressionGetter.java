package org.durcframework.core.expression.getter;

import java.lang.annotation.Annotation;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.annotation.ValueField;
import org.durcframework.core.expression.subexpression.ValueExpression;
import org.springframework.util.StringUtils;

// 构建单值查询条件工厂
public class ValueExpressionGetter implements ExpressionGetter {

		@Override
		public Expression buildExpression(Annotation annotation, String column,
				Object value) {
			if (value == null) {
				return null;
			}
			if (value instanceof String) {
				if (!StringUtils.hasText((String) value)) {
					return null;
				}
			}
			ValueField valueField = (ValueField) annotation;
			String fieldColumn = valueField.column();
			if (StringUtils.hasText(fieldColumn)) {
				column = fieldColumn;
			}
			return new ValueExpression(valueField.joint(), column,
					valueField.equal(), value);
		}

	}