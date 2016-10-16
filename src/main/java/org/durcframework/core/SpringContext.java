package org.durcframework.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 用于获取spring上下文
 * @author hc.tang 2013-4-11
 */
public class SpringContext implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext appCtx)
			throws BeansException {
		applicationContext = appCtx;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clazz) {
		return (T) applicationContext.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

}
