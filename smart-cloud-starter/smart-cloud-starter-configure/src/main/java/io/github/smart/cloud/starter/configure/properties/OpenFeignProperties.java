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
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * openfeign配置属性
 *
 * @author collin
 * @date 2022-06-27
 */
@Getter
@Setter
public class OpenFeignProperties extends Base {

    /**
     * 需要往下一级请求传递的请求头参数名称
     */
    private Set<String> transferHeaderNames;

}