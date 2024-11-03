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
package io.github.smart.cloud.starter.staticdiscovery.autoconfigure;

import io.github.smart.cloud.starter.staticdiscovery.StaticDiscoveryClient;
import io.github.smart.cloud.starter.staticdiscovery.condition.ConditionalOnStaticDiscoveryClient;
import io.github.smart.cloud.starter.staticdiscovery.properties.StaticDiscoveryProperties;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 静态地址发现
 *
 * @author collin
 * @date 2024-11-03
 */
@Configuration
@Conditional(ConditionalOnStaticDiscoveryClient.class)
@AutoConfigureBefore(CompositeDiscoveryClientAutoConfiguration.class)
public class StaticDiscoveryClientAutoConfiguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = StaticDiscoveryProperties.PREFIX)
    public StaticDiscoveryProperties staticDiscoveryProperties() {
        return new StaticDiscoveryProperties();
    }

    @Bean
    @RefreshScope
    public StaticDiscoveryClient staticDiscoveryClient(final StaticDiscoveryProperties staticDiscoveryProperties) {
        return new StaticDiscoveryClient(staticDiscoveryProperties);
    }

}