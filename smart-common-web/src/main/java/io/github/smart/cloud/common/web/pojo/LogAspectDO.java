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
package io.github.smart.cloud.common.web.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 普通接口（除了feign以外的接口）切面日志DO
 *
 * @author collin
 * @date 2019-04-09
 */
@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"url", "method", "cost", "head", "queryParams", "args", "result"})
public class LogAspectDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求路径
     */
    private String url;
    /**
     * http请求方式
     */
    private String method;
    /**
     * 花费时间（毫秒）
     */
    private Long cost;
    /**
     * http头部数据
     */
    private Object head;
    /**
     * url参数
     */
    private String queryParams;
    /**
     * body部分请求体参数
     */
    private Object args;
    /**
     * 请求结果
     */
    private Object result;

}