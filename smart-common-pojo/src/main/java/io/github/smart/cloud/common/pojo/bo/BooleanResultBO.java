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
package io.github.smart.cloud.common.pojo.bo;

import lombok.*;
import io.github.smart.cloud.common.pojo.Base;

/**
 * boolean BO
 *
 * @author collin
 * @date 2019-07-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BooleanResultBO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功（通过）
     */
    private Boolean success;

    /**
     * 提示信息
     */
    private String message;

    public BooleanResultBO(boolean success) {
        this.success = success;
    }

}