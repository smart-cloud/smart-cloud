package org.smartframework.cloud.starter.mybatis.properties;

import org.smartframework.cloud.starter.common.business.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 单数据源配置属性
 *
 * @author liyulin
 * @date 2019年4月24日下午2:59:08
 */
@Getter
@Setter
public class SingleDatasourceProperties extends BaseDto {

	private static final long serialVersionUID = 1L;

	/** jdbc url */
	private String url;

	/** 数据库用户名 */
	private String username;

	/** 数据库密码 */
	private String password;

	/** mapper interface文件所在包名 */
	private String mapperInterfaceLocation;

	/** mapper xml文件所在包名 */
	private String mapperXmlLocation;

	/** 事务所在的顶层包名（多个包名用英文逗号隔开） */
	private String transactionBasePackages;

	/** jdbc驱动类名 */
	private String driverClassName = "com.mysql.cj.jdbc.Driver";

	/** 是否开始seate事务（默认false，不开启） */
	private boolean seataEnable = false;

}