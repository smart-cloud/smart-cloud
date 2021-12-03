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
package org.smartframework.cloud.starter.core.support.annotation;

import java.lang.annotation.*;

/**
 * 自动加载匹配的yaml文件
 *
 * <p>
 * <b>NOTE</b>：该注解必须作用在启动类上才会生效!!!
 * 
 * <p>
 * 此注解的解析不能通过<code>@Import</code>注解；否则，类似<code>@ConditionalOnProperty</code>这种条件注解将不会生效
 *
 * @author liyulin
 * @date 2019-05-11
 * @since YamlEnvironmentPostProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface YamlScan {

	/** 属性名locationPatterns */
	public static final String ATTRIBUTE_LOCATION_PATTERNS = "locationPatterns";

	/** yml文件路径（支持正则表达式） */
	String[] locationPatterns();

}