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
package io.github.smart.cloud.starter.core.business.autoconfigure;

import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.core.business.SmartAsyncConfigurerSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步配置
 *
 * @author collin
 * @date 2021-10-31
 */
@EnableAsync
@Configuration
@ConditionalOnProperty(prefix = "smart.async", name = "enable", havingValue = "true", matchIfMissing = true)
public class SmartAsyncConfigurerSupportAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartAsyncConfigurerSupport smartAsyncConfigurerSupport(final SmartProperties smartProperties) {
        return new SmartAsyncConfigurerSupport(smartProperties.getAsync());
    }

}