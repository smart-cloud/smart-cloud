package org.smartframework.cloud.starter.mybatis.autoconfigure;

import org.smartframework.cloud.starter.mybatis.properties.SmartDatasourceProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Getter;

/**
 * 多数据源配置
 * 
 * @author liyulin
 * @date 2019-05-28
 * @since DataSourceAutoConfiguration
 * @since DataSourceInitializationConfiguration
 */
@Configuration
@EnableConfigurationProperties(SmartDatasourceProperties.class)
@EnableTransactionManagement
@Import({ SmartDataSourceInitializerInvoker.class, SmartDataSourceAutoConfiguration.Registrar.class })
public class SmartDataSourceAutoConfiguration {

	public static class Registrar implements ImportBeanDefinitionRegistrar {

		private static final String BEAN_NAME = SmartDataSourceInitializerPostProcessor.class.getSimpleName();
		@Getter
		private static BeanDefinitionRegistry registry;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			if (!registry.containsBeanDefinition(BEAN_NAME)) {
				SmartDataSourceAutoConfiguration.Registrar.registry = registry;

				GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
				beanDefinition.setBeanClass(SmartDataSourceInitializerPostProcessor.class);
				beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				// We don't need this one to be post processed otherwise it can cause a
				// cascade of bean instantiation that we would rather avoid.
				beanDefinition.setSynthetic(true);
				registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
			}
		}

	}

	@Bean
	public InitTransactionalValue initTransactionalValue() {
		return new InitTransactionalValue();
	}

}