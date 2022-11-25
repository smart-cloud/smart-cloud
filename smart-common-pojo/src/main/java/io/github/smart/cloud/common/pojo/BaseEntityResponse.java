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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.smart.cloud.common.pojo.constant.DateConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体对象对应的响应对象基类
 *
 * @author collin
 * @date 2020-05-24
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@SuperBuilder
public class BaseEntityResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date insertTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date updTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = DateConstant.DEFAULT_DATE_FORMAT)
    private Date delTime;

    /**
     * 新增者
     */
    private Long insertUser;

    /**
     * 更新者
     */
    private Long updUser;

    /**
     * 删除者
     */
    private Long delUser;

    /**
     * 删除状态=={'1':'正常','2':'已删除'}
     */
    private Integer delState;

}