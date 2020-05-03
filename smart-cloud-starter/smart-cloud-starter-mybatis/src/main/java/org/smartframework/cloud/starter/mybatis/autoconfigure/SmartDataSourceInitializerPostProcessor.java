package org.smartframework.cloud.starter.mybatis.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;

/**
 * BeanPostProcessor used to ensure that
 * {@link SmartDataSourceInitializerInvoker} is initialized as soon as a
 * ProxyTransactionManagementConfiguration is.
 * 
 * @author liyulin
 * @date 2019-06-01
 * @since DataSourceInitializerPostProcessor
 */
public class SmartDataSourceInitializerPostProcessor implements BeanPostProcessor, Ordered {

	private BeanFactory beanFactory;
	
	public SmartDataSourceInitializerPostProcessor(final BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ProxyTransactionManagementConfiguration) {
			// force initialization of this bean as soon as we see a
			// ProxyTransactionManagementConfiguration
			beanFactory.getBean(SmartDataSourceInitializerInvoker.class);
		}
		return bean;
	}

}