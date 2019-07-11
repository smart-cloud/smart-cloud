package org.smartframework.cloud.starter.mybatis.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;

/**
 * BeanPostProcessor used to ensure that SmartDataSourceInitializerInvoker is initialized as soon as a ProxyTransactionManagementConfiguration is.
 * 
 * @author liyulin
 * @date 2019年6月1日 上午10:11:12
 * @since DataSourceInitializerPostProcessor
 */
public class SmartDataSourceInitializerPostProcessor implements BeanPostProcessor, Ordered {
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

	@Autowired
	private BeanFactory beanFactory;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof ProxyTransactionManagementConfiguration) {
			// force initialization of this bean as soon as we see a ProxyTransactionManagementConfiguration
			this.beanFactory.getBean(SmartDataSourceInitializerInvoker.class);
		}
		return bean;
	}

}