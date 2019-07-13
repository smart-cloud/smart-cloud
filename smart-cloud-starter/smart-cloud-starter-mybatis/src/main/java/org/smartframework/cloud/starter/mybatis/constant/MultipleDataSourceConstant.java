package org.smartframework.cloud.starter.mybatis.constant;

import lombok.experimental.UtilityClass;

/**
 * 多数据源常量
 * 
 * @author liyulin
 * @date 2019-07-03
 */
@UtilityClass
public class MultipleDataSourceConstant {

	/** 事务bean名称组成部分（后缀） */
	public static final String TRANSACTION_MANAGER_NAME_SUFFIX = "DataSourceTransactionManager";
	/** SqlSessionFactoryBean bean名称组成部分（后缀） */
	public static final String SQL_SESSIONFACTORY_BEAN_NAME_SUFFIX = "SqlSessionFactoryBean";
	/** 默认驱动名 */
	public static final String DEFAULT_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

}