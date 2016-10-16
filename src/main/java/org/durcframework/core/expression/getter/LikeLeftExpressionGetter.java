package org.durcframework.core.expression.getter;

import java.lang.annotation.Annotation;

import org.durcframework.core.expression.Expression;
import org.durcframework.core.expression.annotation.LikeLeftField;
import org.durcframework.core.expression.subexpression.LikeLeftExpression;
import org.springframework.util.StringUtils;

public class LikeLeftExpressionGetter implements ExpressionGetter{

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
			LikeLeftField valueField = (LikeLeftField) annotation;
			String fieldColumn = valueField.column();
			if (StringUtils.hasText(fieldColumn)) {
				column = fieldColumn;
			}
			return new LikeLeftExpression(valueField.joint(),column, value);
		}
		
	}