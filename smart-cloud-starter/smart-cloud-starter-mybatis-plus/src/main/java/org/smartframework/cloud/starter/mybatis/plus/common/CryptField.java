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
package org.smartframework.cloud.starter.mybatis.plus.common;

import lombok.*;

import java.io.Serializable;

/**
 * 隐私字段类型
 *
 * @author collin
 * @date 2022-02-06
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CryptField implements Serializable {

    /**
     * 隐私字段值
     */
    private String value;

    public static CryptField of(String value) {
        return new CryptField(value);
    }

}