package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.experimental.UtilityClass;

/**
 * yml文件公共属性配置定义
 *
 * @author liyulin
 * @date 2019年4月14日下午4:45:04
 */
@Data
@ConfigurationProperties(prefix = SmartConstant.SMART_PROPERTIES_PREFIX)
public class SmartProperties {

	/** id生成器数据机器标识配置 */
	private Long dataMachineId;
	/** @Async配置开关 */
	private boolean async = true;
	/** api配置 */
	private ApiProperties api = new ApiProperties();
	/** 切面配置 */
	private AspectProperties aspect = new AspectProperties();
	/** swagger配置 */
	private SwaggerProperties swagger = new SwaggerProperties();
	/** xxl-job配置 */
	private XxlJobProperties xxlJob = new XxlJobProperties();

	@UtilityClass
	public static final class PropertiesName {
		public static final String DATA_MACHINE_ID = "dataMachineId";
		public static final String API_VERSION = "apiVersion";
	}

}