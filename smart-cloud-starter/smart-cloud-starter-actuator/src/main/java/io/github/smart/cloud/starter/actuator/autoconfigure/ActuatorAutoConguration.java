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
package io.github.smart.cloud.starter.actuator.autoconfigure;

import io.github.smart.cloud.starter.actuator.properties.HealthProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * actuator自动配置类
 *
 * @author collin
 * @date 2024-01-16
 */
@Configuration
public class ActuatorAutoConguration {

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "smart.health")
    public HealthProperties healthProperties() {
        return new HealthProperties();
    }

}