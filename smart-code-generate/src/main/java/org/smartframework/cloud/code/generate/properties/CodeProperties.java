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
package org.smartframework.cloud.code.generate.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smartframework.cloud.code.generate.enums.GenerateTypeEnum;

import java.util.Map;

/**
 * @author liyulin
 * @desc 待生成的代码信息
 * @date 2019/11/08
 */
@Getter
@Setter
@ToString
public class CodeProperties {
    /**
     * 代码类作者
     */
    private String author;
    /**
     * 生成类型 {@link GenerateTypeEnum}：1、数据库整个表全部生成；2、只生成指定的表；3、除了指定的表，全部生成
     */
    private Integer type;
    /**
     * 指定要生成的表，多个表用英文逗号（,）隔开
     */
    private String specifiedTables;
    /**
     * 表字段脱敏规则
     */
    private Map<String, Map<String, String>> mask;
    /**
     * 启动类包名
     */
    private String mainClassPackage;
    /**
     * 工程信息
     */
    private ProjectProperties project;
}