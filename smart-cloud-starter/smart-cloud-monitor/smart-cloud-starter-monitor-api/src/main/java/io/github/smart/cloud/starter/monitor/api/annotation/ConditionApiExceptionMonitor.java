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
package io.github.smart.cloud.starter.monitor.api.annotation;

import io.github.smart.cloud.starter.monitor.api.properties.ApiMonitorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 接口异常监控启用条件注解
 *
 * @author collin
 * @date 2024-07-01
 */
@ConditionalOnProperty(prefix = ApiMonitorProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public @interface ConditionApiExceptionMonitor {
}