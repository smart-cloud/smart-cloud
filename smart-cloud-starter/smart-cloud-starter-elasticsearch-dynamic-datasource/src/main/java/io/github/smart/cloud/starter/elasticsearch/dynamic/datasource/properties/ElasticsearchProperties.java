package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties;

import io.github.smart.cloud.common.pojo.Base;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Elasticsearch配置属性
 *
 * @author collin
 * @date 2022-06-02
 */
@Getter
@Setter
public class ElasticsearchProperties extends Base {

    /**
     * es实例列表（使用逗号隔开）
     */
    private List<String> uris = new ArrayList(Collections.singletonList("http://localhost:9200"));

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * connect超时时间（默认1秒）
     */
    private Duration connectionTimeout = Duration.ofSeconds(1L);
    /**
     * socket超时时间（默认30秒）
     */
    private Duration socketTimeout = Duration.ofSeconds(30L);
    /**
     * es请求路径前缀
     */
    private String pathPrefix;

}