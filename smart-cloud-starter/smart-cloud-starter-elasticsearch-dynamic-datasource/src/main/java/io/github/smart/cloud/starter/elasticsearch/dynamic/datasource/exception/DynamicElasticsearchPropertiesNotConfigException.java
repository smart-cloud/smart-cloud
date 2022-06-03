package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception;

import io.github.smart.cloud.exception.BaseException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.constants.ElasticsearchReturnCodes;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.DynamicElasticsearchProperties;

/**
 * elasticsearch 动态数据源属性未配置异常
 *
 * @author collin
 * @date 2022-06-03
 * @see DynamicElasticsearchProperties
 */
public class DynamicElasticsearchPropertiesNotConfigException extends BaseException {

    public DynamicElasticsearchPropertiesNotConfigException() {
        setCode(ElasticsearchReturnCodes.DYNAMIC_ELASTICSEARCH_PROPERTIES_NOT_CONFIG);
    }

}
