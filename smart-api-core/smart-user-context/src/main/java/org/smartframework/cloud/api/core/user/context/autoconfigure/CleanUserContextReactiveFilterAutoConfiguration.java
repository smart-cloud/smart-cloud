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
package org.smartframework.cloud.api.core.user.context.autoconfigure;

import org.smartframework.cloud.api.core.user.context.filter.CleanUserContextReactiveFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 清除用户上下文过滤器配置
 *
 * @author collin
 * @date 2021-10-31
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CleanUserContextReactiveFilterAutoConfiguration {

    @Bean
    public CleanUserContextReactiveFilter cleanUserContextReactiveFilter() {
        return new CleanUserContextReactiveFilter();
    }

}