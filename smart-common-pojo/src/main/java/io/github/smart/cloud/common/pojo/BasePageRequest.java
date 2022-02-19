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
package io.github.smart.cloud.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页请求参数基类
 *
 * @author collin
 * @date 2020-05-07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePageRequest extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 第几页，第1页值为1
     */
    @NotNull
    @Min(value = 1)
    private Integer pageNum;

    /**
     * 页面数据大小
     */
    @NotNull
    @Min(value = 1)
    private Integer pageSize;

}