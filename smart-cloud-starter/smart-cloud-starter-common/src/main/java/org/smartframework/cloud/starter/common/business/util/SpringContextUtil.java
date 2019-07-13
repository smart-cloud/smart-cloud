package org.smartframework.cloud.starter.common.business.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Spring工具类
 *
 * @author liyulin
 * @date 2019-04-03
 */
public class SpringContextUtil implements ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (SpringContextUtil.applicationContext == null) {
			SpringContextUtil.applicationContext = event.getApplicationContext();
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取class对应的bean
	 *
	 * @param <T> 类型
	 * @return bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(requiredType);
	}

}