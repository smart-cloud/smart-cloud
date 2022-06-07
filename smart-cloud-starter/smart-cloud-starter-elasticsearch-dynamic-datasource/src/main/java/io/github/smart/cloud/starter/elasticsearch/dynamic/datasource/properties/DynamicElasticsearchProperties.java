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