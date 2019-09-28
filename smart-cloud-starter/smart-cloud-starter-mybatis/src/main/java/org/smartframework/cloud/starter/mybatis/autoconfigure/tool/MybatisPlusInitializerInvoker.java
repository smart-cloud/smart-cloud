package org.smartframework.cloud.starter.mybatis.autoconfigure.tool;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.smartframework.cloud.starter.common.support.bean.UniqueBeanNameGenerator;
import org.smartframework.cloud.starter.mybatis.autoconfigure.SmartDataSourceAutoConfiguration;
import org.smartframework.cloud.starter.mybatis.properties.SingleDatasourceProperties;
import org.smartframework.cloud.starter.mybatis.properties.SmartDatasourceProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import lombok.extern.slf4j.Slf4j;

/**
 * mybatis plus方式初始化
 * 
 * @author liyulin
 * @date 2019-07-17
 */
@Slf4j
public class MybatisPlusInitializerInvoker extends AbstractInitializerInvoker {

	public MybatisPlusInitializerInvoker(SmartDatasourceProperties smartDatasourceProperties,
			ConfigurableBeanFactory beanFactory) {
		super(smartDatasourceProperties, beanFactory);
	}

	@Override
	protected void registerSqlSessionFactoryBean(String beanName, SingleDatasourceProperties dataSourceProperties,
			DataSource dataSource) {
		// 构建bean对象
		MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] mapperXmlLocationResources = resolver.getResources(dataSourceProperties.getMapperXmlLocation());
			sqlSessionFactoryBean.setMapperLocations(mapperXmlLocationResources);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		sqlSessionFactoryBean.setPlugins(mybatisSqlLogInterceptor, buildPaginationInterceptor());

		// 注册bean
		registerBean(beanName, sqlSessionFactoryBean);
	}

	@Override
	protected void registerMapperScannerConfigurer(String serviceName, String sqlSessionFactoryBeanName,
			SingleDatasourceProperties dataSourceProperties) {
		String mapperScannerConfigurerBeanName = generateBeanName(serviceName,
				MapperScannerConfigurer.class.getSimpleName());
		// 构建bean对象
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
		mapperScannerConfigurer.setBasePackage(dataSourceProperties.getMapperInterfaceLocation());
		mapperScannerConfigurer.setNameGenerator(new UniqueBeanNameGenerator());
		// 注册bean
		registerBean(mapperScannerConfigurerBeanName, mapperScannerConfigurer);
		// 注入mapper接口
		mapperScannerConfigurer.postProcessBeanDefinitionRegistry(SmartDataSourceAutoConfiguration.Registrar.getRegistry());
	}

	/**
	 * 构建分页拦截器
	 * 
	 * @return
	 */
	private PaginationInterceptor buildPaginationInterceptor() {
		return new PaginationInterceptor();
	}
	
}