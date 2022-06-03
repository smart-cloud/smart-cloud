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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.core;

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.DynamicElasticsearchPropertiesNotConfigException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.ElasticsearchDataSourceNotFoundException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.exception.ElasticsearchDatasourceKeyNotExistException;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.DynamicElasticsearchProperties;
import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * elasticsearch动态RestHighLevelClient
 *
 * @author collin
 * @date 2022-06-03
 */
@RequiredArgsConstructor
public class DynamicRestHighLevelClient implements InitializingBean, DisposableBean {

    /**
     * RestHighLevelClient路由，key为数据源名
     */
    private Map<String, RestHighLevelClient> restHighLevelClientRoute = new HashMap<>();
    private final DynamicElasticsearchProperties dynamicElasticsearchProperties;

    public RestHighLevelClient determine() {
        String dsKey = DynamicElasticsearchDataSourceContextHolder.peek();
        return getRestHighLevelClient(dsKey);
    }

    public final RestHighLevelClient getRestHighLevelClient(String dsKey) {
        if (StringUtils.isBlank(dsKey)) {
            throw new ElasticsearchDatasourceKeyNotExistException();
        }

        RestHighLevelClient restHighLevelClient = restHighLevelClientRoute.get(dsKey);
        if (restHighLevelClient == null) {
            throw new ElasticsearchDataSourceNotFoundException(dsKey);
        }

        return restHighLevelClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, ElasticsearchProperties> datasources = dynamicElasticsearchProperties.getDatasources();
        if (MapUtils.isEmpty(datasources)) {
            throw new DynamicElasticsearchPropertiesNotConfigException();
        }

        datasources.forEach((dsKey, elasticsearchProperties) -> {
            restHighLevelClientRoute.put(dsKey, buildRestHighLevelClient(elasticsearchProperties));
        });
    }

    @Override
    public void destroy() throws Exception {
        restHighLevelClientRoute.values().forEach(client -> {
            try {
                client.close();
            } catch (IOException e) {
            }
        });
    }

    private RestHighLevelClient buildRestHighLevelClient(ElasticsearchProperties elasticsearchProperties) {
        RestClientBuilderCustomizer restClientBuilderCustomizer = new SmartRestClientBuilderCustomizer(elasticsearchProperties);
        RestClientBuilder restClientBuilder = elasticsearchRestClientBuilder(restClientBuilderCustomizer, elasticsearchProperties);
        return new RestHighLevelClient(restClientBuilder);
    }

    private RestClientBuilder elasticsearchRestClientBuilder(RestClientBuilderCustomizer builderCustomizer, ElasticsearchProperties properties) {
        Stream<RestClientBuilderCustomizer> builderCustomizers = Stream.of(builderCustomizer);
        HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(hosts);
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            builderCustomizers.forEach(customizer -> customizer.customize(httpClientBuilder));
            return httpClientBuilder;
        });
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            builderCustomizers.forEach(customizer -> customizer.customize(requestConfigBuilder));
            return requestConfigBuilder;
        });
        if (properties.getPathPrefix() != null) {
            builder.setPathPrefix(properties.getPathPrefix());
        }
        builderCustomizers.forEach(customizer -> customizer.customize(builder));
        return builder;
    }

    private HttpHost createHttpHost(String uri) {
        try {
            return createHttpHost(URI.create(uri));
        } catch (IllegalArgumentException e) {
            return HttpHost.create(uri);
        }
    }

    private HttpHost createHttpHost(URI uri) {
        if (StringUtils.isBlank(uri.getUserInfo())) {
            return HttpHost.create(uri.toString());
        }
        try {
            return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
                    uri.getQuery(), uri.getFragment()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}