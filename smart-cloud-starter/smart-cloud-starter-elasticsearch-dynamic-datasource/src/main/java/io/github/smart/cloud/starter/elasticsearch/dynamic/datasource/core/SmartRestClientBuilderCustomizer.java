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

import io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;

@RequiredArgsConstructor
public class SmartRestClientBuilderCustomizer implements RestClientBuilderCustomizer {

    private static final PropertyMapper PROPERTY_MAPPER = PropertyMapper.get();

    private final ElasticsearchProperties properties;

    @Override
    public void customize(RestClientBuilder builder) {
    }

    @Override
    public void customize(HttpAsyncClientBuilder builder) {
        builder.setDefaultCredentialsProvider(new PropertiesCredentialsProvider(this.properties));
    }

    @Override
    public void customize(RequestConfig.Builder builder) {
        PROPERTY_MAPPER.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
                .to(builder::setConnectTimeout);
        PROPERTY_MAPPER.from(properties::getSocketTimeout).whenNonNull().asInt(Duration::toMillis)
                .to(builder::setSocketTimeout);
    }

}