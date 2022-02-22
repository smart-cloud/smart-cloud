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
package io.github.smart.cloud.test.core.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 上传文件file参数
 *
 * @author collin
 * @date 2021-06-23
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class FileVO implements java.io.Serializable {

    /**
     * 表单文件参数名
     */
    private String name;
    /**
     * 文件
     */
    private java.io.File file;

}