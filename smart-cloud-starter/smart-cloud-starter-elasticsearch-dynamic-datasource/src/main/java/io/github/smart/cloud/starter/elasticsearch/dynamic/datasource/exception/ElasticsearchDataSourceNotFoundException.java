package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception;

import io.github.smart.cloud.exception.BaseException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.constants.ElasticsearchReturnCodes;

/**
 * elasticsearch 数据源未找到异常
 *
 * @author collin
 * @date 2022-06-03
 */
public class ElasticsearchDataSourceNotFoundException extends BaseException {

    public ElasticsearchDataSourceNotFoundException(String dsKey) {
        setCode(ElasticsearchReturnCodes.ELASTICSEARCH_DS_NOT_FOUND);
        setArgs(new Object[]{dsKey});
    }

}
