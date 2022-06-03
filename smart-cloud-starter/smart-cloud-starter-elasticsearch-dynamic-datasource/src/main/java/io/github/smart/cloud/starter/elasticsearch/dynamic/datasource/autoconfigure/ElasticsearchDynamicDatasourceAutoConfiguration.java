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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.autoconfigure;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core.DynamicRestHighLevelClient;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.DynamicElasticsearchProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * elasticsearch动态配置
 *
 * @author collin
 * @date 2022-06-02
 */
@Configuration
@Import(DynamicElasticsearchInterceptorAutoConfiguration.class)
@EnableConfigurationProperties(DynamicElasticsearchProperties.class)
@ConditionalOnProperty(prefix = DynamicElasticsearchProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class ElasticsearchDynamicDatasourceAutoConfiguration {

    @Bean
    public DynamicRestHighLevelClient dynamicRestHighLevelClient(final DynamicElasticsearchProperties dynamicElasticsearchProperties) {
        return new DynamicRestHighLevelClient(dynamicElasticsearchProperties);
    }

}