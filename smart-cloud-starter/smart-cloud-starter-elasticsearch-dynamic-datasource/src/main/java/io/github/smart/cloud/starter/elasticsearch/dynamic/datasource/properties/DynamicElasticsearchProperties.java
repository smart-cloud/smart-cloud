package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties;

import io.github.smart.cloud.common.pojo.Base;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * elasticsearch动态数据源配置
 *
 * @author collin
 * @daten 2022-06-02
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = DynamicElasticsearchProperties.PREFIX)
public class DynamicElasticsearchProperties extends Base {

    /**
     * elasticsearch配置属性前缀
     */
    public static final String PREFIX = "smart.elasticsearch";

    private final Map<String, ElasticsearchProperties> datasources = new LinkedHashMap<>();

}