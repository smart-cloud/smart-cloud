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