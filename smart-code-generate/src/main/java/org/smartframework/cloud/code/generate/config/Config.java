package org.smartframework.cloud.code.generate.config;

import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;

import java.nio.charset.StandardCharsets;

/**
 * 配置常量
 *
 * @author liyulin
 * @date 2019-07-15
 */
public interface Config {

    /**
     * 默认编码（utf-8）
     */
    String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();
    /**
     * 代码生成日期格式
     */
    String CODE_CREATE_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 配置文件所在路径
     */
    String CONFIG_PATH = "config/";
    /**
     * 总配置文件名
     */
    String CONFIG_NAME = "generate_config";
    /**
     * 配置文key
     */
    String PROPERTIES_KEY = "config.yaml";
    /**
     * 模板所在位置
     */
    String TEMPLATE_PATH = "/template";
    /**
     * 多个表用隔开的分隔符
     */
    String TABLES_SEPARATOR = ",";

    String ENTITY_CLASS_SUFFIX = "Entity";
    String MAPPER_CLASS_SUFFIX = "BaseMapper";
    String BASE_RESPVO_CLASS_SUFFIX = "BaseRespVO";

    String ENTITY_PACKAGE_SUFFIX = ".entity.base";
    String MAPPER_PACKAGE_SUFFIX = ".mapper.base";
    String BASE_RESPVO_PACKAGE_SUFFIX = ".response.base";

    /**
     * 模板文件名
     */
    interface Template {
        /**
         * mapper
         */
        String BASE_MAPPER = "BaseMapper.ftl";
        /**
         * respbody
         */
        String BASE_RESPBODY = "BaseRespVO.ftl";
        /**
         * entity
         */
        String ENTITY = "Entity.ftl";
    }

    /**
     * mask相关包名
     */
    interface MaskPackage {
        /**
         * MaskRule包名
         */
        String MASK_RULE = MaskRule.class.getTypeName();
        /**
         * MaskLog包名
         */
        String MASK_LOG = MaskLog.class.getTypeName();
    }

}