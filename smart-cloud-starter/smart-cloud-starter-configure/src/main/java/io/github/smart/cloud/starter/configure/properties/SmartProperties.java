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
package io.github.smart.cloud.starter.configure.properties;

import io.github.smart.cloud.common.pojo.Base;
import io.github.smart.cloud.starter.configure.constants.SmartConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * yml文件公共属性配置定义
 *
 * @author collin
 * @date 2019-04-14
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SmartConstant.SMART_PROPERTIES_PREFIX)
public class SmartProperties extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 异步配置
     */
    @NestedConfigurationProperty
    private AsyncProperties async = new AsyncProperties();

    /**
     * 切面配置
     */
    @NestedConfigurationProperty
    private LogProperties log = new LogProperties();
    /**
     * xxl-job配置
     */
    @NestedConfigurationProperty
    private XxlJobProperties xxlJob = new XxlJobProperties();
    /**
     * 多语言配置
     */
    @NestedConfigurationProperty
    private LocaleProperties locale = new LocaleProperties();
    /**
     * mock配置
     */
    @NestedConfigurationProperty
    private MockProperties mock = new MockProperties();
    /**
     * openfeign配置
     */
    @NestedConfigurationProperty
    private OpenFeignProperties feign = new OpenFeignProperties();
    /**
     * 表字段加解密相关配置
     */
    @NestedConfigurationProperty
    private CryptFieldProperties cryptField = new CryptFieldProperties();

}