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
package org.smartframework.cloud.utility.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 工具类注入
 *
 * @author liyulin
 * @date 2019-07-12
 */
@Configuration
public class UtilAutoConfigure {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Bean
    public I18nUtil i18nUtil() {
        return new I18nUtil();
    }

}