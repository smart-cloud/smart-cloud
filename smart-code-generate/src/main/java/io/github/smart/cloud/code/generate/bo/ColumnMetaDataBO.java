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
package io.github.smart.cloud.code.generate.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表字段元数据信息
 *
 * @author collin
 * @date 2019-07-15
 */
@Getter
@Setter
@ToString
public class ColumnMetaDataBO {

    /**
     * 表字段名
     */
    private String name;
    /**
     * 数据类型
     */
    private Integer jdbcType;
    /**
     * 字段备注
     */
    private String comment;
    /**
     * 字段长度
     */
    private Integer length;
    /**
     * 是否为主键
     */
    private Boolean primaryKey;

}