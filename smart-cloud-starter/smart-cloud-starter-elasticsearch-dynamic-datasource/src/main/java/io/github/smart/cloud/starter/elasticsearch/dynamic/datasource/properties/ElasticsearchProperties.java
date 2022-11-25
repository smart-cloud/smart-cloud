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
package io.github.smart.cloud.starter.elasticsearch.dynamic.datasource.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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
@ToString
public class ElasticsearchProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * es实例列表（使用逗号隔开）
     */
    private List<String> uris = new ArrayList<>(Collections.singletonList("http://localhost:9200"));

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