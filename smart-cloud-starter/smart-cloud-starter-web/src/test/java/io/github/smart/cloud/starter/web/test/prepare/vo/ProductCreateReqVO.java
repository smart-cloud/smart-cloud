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
package io.github.smart.cloud.starter.web.test.prepare.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import io.github.smart.cloud.common.pojo.Base;

import javax.validation.constraints.NotBlank;

/**
 * 创建产品
 *
 * @author collin
 * @date 2021-10-28
 */
@Getter
@Setter
public class ProductCreateReqVO extends Base {

    @NotBlank
    @Length(max = 64)
    private String name;

    @NotBlank
    @Length(max = 128)
    private String desc;

}