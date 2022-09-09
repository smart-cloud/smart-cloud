/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.constants;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.DynamicElasticsearchPropertiesNotConfigException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.ElasticsearchDataSourceNotFoundException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.ElasticsearchDatasourceKeyNotExistException;

/**
 * smart-cloud-starter-elasticsearch-dynamic-datasource模块返回码
 *
 * @author collin
 * @date 2021-10-31
 */
public class ElasticsearchReturnCodes {

    /**
     * elasticsearch ds key不存在
     *
     * @see ElasticsearchDatasourceKeyNotExistException
     */
    public static final String ELASTICSEARCH_DS_KEY_CAN_NOT_BLANK = "3001";

    /**
     * elasticsearch 数据源未找到
     *
     * @see ElasticsearchDataSourceNotFoundException
     */
    public static final String ELASTICSEARCH_DS_NOT_FOUND = "3002";

    /**
     * elasticsearch 动态数据源属性未配置
     *
     * @see DynamicElasticsearchPropertiesNotConfigException
     */
    public static final String DYNAMIC_ELASTICSEARCH_PROPERTIES_NOT_CONFIG = "3003";

    private ElasticsearchReturnCodes() {
    }

}