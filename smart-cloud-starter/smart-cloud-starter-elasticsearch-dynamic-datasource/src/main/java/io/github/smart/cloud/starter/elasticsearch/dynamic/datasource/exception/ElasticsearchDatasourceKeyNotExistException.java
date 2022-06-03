package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception;

import io.github.smart.cloud.exception.BaseException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.constants.ElasticsearchReturnCodes;

/**
 * elasticsearch 数据源key不存在异常
 *
 * @author collin
 * @date 2022-06-03
 */
public class ElasticsearchDatasourceKeyNotExistException extends BaseException {

    public ElasticsearchDatasourceKeyNotExistException() {
        setCode(ElasticsearchReturnCodes.ELASTICSEARCH_DS_KEY_CAN_NOT_BLANK);
    }

}