package org.smartframework.cloud.starter.configure.properties;

import org.smartframework.cloud.common.pojo.dto.BaseDto;
import org.smartframework.cloud.starter.configure.constants.SmartConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * yml文件公共属性配置定义
 *
 * @author liyulin
 * @date 2019-04-14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SmartConstant.SMART_PROPERTIES_PREFIX)
public class SmartProperties extends BaseDto {

	private static final long serialVersionUID = 1L;
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
	/** 多语言配置 */
	private LocaleProperties locale = new LocaleProperties();

	@UtilityClass
	public static final class PropertiesName {
		public static final String DATA_MACHINE_ID = "dataMachineId";
		public static final String API_VERSION = "apiVersion";
	}

}