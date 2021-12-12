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
package org.smartframework.cloud.code.generate.bo.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * entity属性信息
 *
 * @author collin
 * @date 2019-07-13
 */
@Getter
@Setter
@ToString
public class EntityAttributeBO {

    /**
     * 是否为主键
     */
    private Boolean primaryKey;
    /**
     * 表字段对应的java属性名
     */
    private String name;
    /**
     * 表字段名称
     */
    private String columnName;
    /**
     * 类型
     */
    private String javaType;
    /**
     * 备注
     */
    private String comment;
    /**
     * mask规则
     */
    private String maskRule;

}