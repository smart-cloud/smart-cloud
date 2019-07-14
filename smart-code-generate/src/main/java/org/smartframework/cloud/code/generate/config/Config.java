package org.smartframework.cloud.code.generate.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Config {
	/** 模板所在位置 */
	public static final String TEMPLATE_PATH = "/template";
	
	public static final String ENTITY_CLASS_SUFFIX = "Entity";
	public static final String MAPPER_CLASS_SUFFIX = "BaseMapper";
	public static final String BASE_RESPBODY_CLASS_SUFFIX = "BaseRespBody";

	public static final String MAIN_CLASS_PACKAGE = "org.smartframework.cloud.examples.mall.service.product";

	public static final String ENTITY_PACKAGE_SUFFIX = ".entity.base";
	public static final String MAPPER_PACKAGE_SUFFIX = ".mapper.base";
	public static final String BASE_RESPBODY_PACKAGE_SUFFIX = ".response.base";
}