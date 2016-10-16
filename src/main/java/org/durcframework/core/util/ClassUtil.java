package org.durcframework.core.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.durcframework.core.expression.ExpressionQuery;

/**
 * 
 * @author thc 2011-10-22
 */
public class ClassUtil {
	
	private static final String PREFIX_GET = "get";

	private ClassUtil() {
	};

	private static String classPath = "";

	private static ClassUtil classUtil = new ClassUtil();


	/**
	 * 返回定义类时的泛型参数的类型. <br/>
	 * 如:定义一个BookManager类<br>
	 * <code>{@literal public BookManager extends GenricManager<Book,Address>}{...} </code><br>
	 * 调用getSuperClassGenricType(getClass(),0)将返回Book的Class类型<br>
	 * 调用getSuperClassGenricType(getClass(),1)将返回Address的Class类型
	 * 
	 * @param clazz
	 *            从哪个类中获取
	 * @param index
	 *            泛型参数索引,从0开始
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index)
			throws IndexOutOfBoundsException {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	private String _getClassPath() {
		if ("".equals(classPath)) {
			classPath = getClass().getClassLoader().getResource("").getPath();
		}
		return classPath;
	}

	/**
	 * 获取class根目录
	 * 
	 * @return
	 */
	public static String getClassRootPath() {
		return classUtil._getClassPath();
	}
	
	/**
	 * 返回类名并且第一个字母小写
	 * @param clazz
	 * @return
	 * @author hc.tang
	 */
	public static String getClassSimpleName(Class<?> clazz) {
		String className = clazz.getSimpleName();
		return className.substring(0, 1).toLowerCase()
				+ className.substring(1);
	}
	
	public static Map<String, Object> convertObj2Map(Object obj){
		if(obj == null){
			return Collections.emptyMap();
		}
		Method[] methods = obj.getClass().getDeclaredMethods();
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		try {
			for (Method method : methods) {
				String methodName = method.getName();
	
				if (methodName.startsWith(PREFIX_GET)) {
					String fieldName = buildFieldName(methodName);
					Object value = method.invoke(obj, new Object[] {});
					map.put(fieldName, value);
				}
			}
		}catch (Exception e) {
			return Collections.emptyMap();
		}
		
		return map;
	}
	
	// 构建列名
	private static String buildFieldName(String methodName) {
		return methodName.substring(3, 4).toLowerCase()
				+ methodName.substring(4);

	}
	
	
	public static void main(String[] args) {
		System.out.println(getClassRootPath());
		
		ExpressionQuery query = new ExpressionQuery();
		
		Map<String, Object> map = convertObj2Map(query);
		System.out.println(map);
	}

}
