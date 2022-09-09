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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.util;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicRestHighLevelClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.IOException;

/**
 * Elasticsearch客户端工具类
 *
 * @author collin
 * @date 2022-06-05
 */
@Slf4j
public class ElasticsearchClientUtil {

    @Setter
    private static DynamicRestHighLevelClient dynamicRestHighLevelClient;

    private ElasticsearchClientUtil() {
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public static CreateIndexResponse createIndex(String indexName) throws IOException {
        if (existIndex(indexName)) {
            log.warn("index[{}] already exists, skip creation.", indexName);
            return new CreateIndexResponse(false, false, indexName);
        }

        return dynamicRestHighLevelClient.determine().indices().create(new CreateIndexRequest(indexName), RequestOptions.DEFAULT);
    }

    /**
     * 判断索引是否存在
     *
     * @param indexName 要判断是否存在的索引的名字
     * @return 返回是否存在。
     * <ul>
     * 	<li>true:存在</li>
     * 	<li>false:不存在</li>
     * </ul>
     */
    public static boolean existIndex(String indexName) {
        try {
            return dynamicRestHighLevelClient.determine().indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("indexName={}", indexName, e);
            return false;
        }
    }

}