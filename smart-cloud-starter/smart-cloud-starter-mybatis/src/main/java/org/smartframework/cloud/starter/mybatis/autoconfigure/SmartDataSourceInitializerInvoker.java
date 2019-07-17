package org.smartframework.cloud.starter.mybatis.autoconfigure;

import org.smartframework.cloud.starter.mybatis.autoconfigure.tool.MapperInitializerInvoker;
import org.smartframework.cloud.starter.mybatis.autoconfigure.tool.MybatisPlusInitializerInvoker;
import org.smartframework.cloud.starter.mybatis.enums.ToolTypeEnum;
import org.smartframework.cloud.starter.mybatis.properties.SmartDatasourceProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * 多数据源bean初始化
 * 
 * @author liyulin
 * @date 2019-05-28
 * @since DataSourceInitializerInvoker
 */
public class SmartDataSourceInitializerInvoker {

	public SmartDataSourceInitializerInvoker(final SmartDatasourceProperties smartDatasourceProperties,
			final ConfigurableBeanFactory beanFactory) {
		if (ToolTypeEnum.MAPPER.getType().equals(smartDatasourceProperties.getToolType())) {
			new MapperInitializerInvoker(smartDatasourceProperties, beanFactory);
		} else {
			new MybatisPlusInitializerInvoker(smartDatasourceProperties, beanFactory);
		}
	}

}