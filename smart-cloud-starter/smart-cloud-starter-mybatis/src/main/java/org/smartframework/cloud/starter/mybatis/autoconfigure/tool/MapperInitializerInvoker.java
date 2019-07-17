package org.smartframework.cloud.starter.mybatis.autoconfigure.tool;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.smartframework.cloud.starter.common.support.bean.UniqueBeanNameGenerator;
import org.smartframework.cloud.starter.mybatis.autoconfigure.SmartDataSourceAutoConfiguration;
import org.smartframework.cloud.starter.mybatis.properties.SingleDatasourceProperties;
import org.smartframework.cloud.starter.mybatis.properties.SmartDatasourceProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.github.pagehelper.PageInterceptor;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * 通用mapper方式初始化
 * 
 * @author liyulin
 * @date 2019-07-17
 */
@Slf4j
public class MapperInitializerInvoker extends AbstractInitializerInvoker {

	public MapperInitializerInvoker(SmartDatasourceProperties smartDatasourceProperties,
			ConfigurableBeanFactory beanFactory) {
		super(smartDatasourceProperties, beanFactory);
	}

	@Override
	protected void registerSqlSessionFactoryBean(String beanName, SingleDatasourceProperties dataSourceProperties,
			DataSource dataSource) {
		// 构建bean对象
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] mapperXmlLocationResources = resolver.getResources(dataSourceProperties.getMapperXmlLocation());
			sqlSessionFactoryBean.setMapperLocations(mapperXmlLocationResources);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { mybatisSqlLogInterceptor, buildPageInterceptor() });
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
		Properties properties = new Properties();
		properties.setProperty("IDENTITY", "MYSQL");
		properties.setProperty("notEmpty", "true");
		properties.setProperty("safeUpdate", "true");
		mapperScannerConfigurer.setProperties(properties);
		mapperScannerConfigurer.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
		mapperScannerConfigurer.setBasePackage(dataSourceProperties.getMapperInterfaceLocation());
		mapperScannerConfigurer.setNameGenerator(new UniqueBeanNameGenerator());
		// 注册bean
		registerBean(mapperScannerConfigurerBeanName, mapperScannerConfigurer);
		// 注入mapper接口
		mapperScannerConfigurer
				.postProcessBeanDefinitionRegistry(SmartDataSourceAutoConfiguration.Registrar.getRegistry());
	}

	/**
	 * 构建分页拦截器
	 * 
	 * @return
	 */
	private PageInterceptor buildPageInterceptor() {
		PageInterceptor pageHelper = new PageInterceptor();
		Properties p = new Properties();
		// 分页合理化参数
		p.setProperty("reasonable", "true");
		p.setProperty("supportMethodsArguments", "true");
		p.setProperty("params", "count=countSql");
		pageHelper.setProperties(p);
		return pageHelper;
	}

}