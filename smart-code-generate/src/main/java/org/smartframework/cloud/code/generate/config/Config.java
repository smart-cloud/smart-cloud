package org.smartframework.cloud.code.generate.config;

import java.nio.charset.StandardCharsets;

import lombok.experimental.UtilityClass;

/**
 * 配置常量
 *
 * @author liyulin
 * @date 2019-07-15
 */
@UtilityClass
public class Config {

	/** 默认编码（utf-8） */
	public static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
	/** 代码生成日期格式 */
	public static final String CREATEDATE_FORMAT = "yyyy-MM-dd";
	/** 配置文件所在路径 */
	public static final String CONFIG_PATH = "config/";
	/** 总配置文件名 */
	public static final String CONFIG_NAME = CONFIG_PATH + "generate_config";
	/** 配置文key */
	public static final String PROPERTIES_KEY = "config.properties";
	/** 模板所在位置 */
	public static final String TEMPLATE_PATH = "/template";

	public static final String ENTITY_CLASS_SUFFIX = "Entity";
	public static final String MAPPER_CLASS_SUFFIX = "BaseMapper";
	public static final String BASE_RESPBODY_CLASS_SUFFIX = "BaseRespBody";

	public static final String ENTITY_PACKAGE_SUFFIX = ".entity.base";
	public static final String MAPPER_PACKAGE_SUFFIX = ".mapper.base";
	public static final String BASE_RESPBODY_PACKAGE_SUFFIX = ".response.base";

	/** 模板文件名 */
	public static final class Template {
		/** mapper */
		public static final String BASE_MAPPER = "BaseMapper.ftl";
		/** respbody */
		public static final String BASE_RESPBODY = "BaseRespBody.ftl";
		/** entity */
		public static final String ENTITY = "Entity.ftl";
	}

}