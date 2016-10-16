package org.durcframework.core.expression;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.durcframework.core.expression.getter.ExpressionGetter;

/**
 * 负责存放注解与ExpressionGetter
 * @author hc.tang
 * 2013年11月14日
 *
 */
public class AnnoExprStore {
	
	private static Map<String, ExpressionGetter> map = new HashMap<String, ExpressionGetter>();
	
	/**
	 * 通过注解获取
	 * @param annotation
	 * @return
	 */
	public static ExpressionGetter get(Annotation annotation){
		return map.get(annotation.annotationType().getSimpleName());
	}
	
	/**
	 * 保存
	 * @param clazz 注解的class
	 * @param getter ExpressionGetter
	 */
	public static void addExpressionGetter(Class<?> clazz,ExpressionGetter getter) {
		map.put(clazz.getSimpleName(), getter);
	}
}
