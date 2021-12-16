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
package org.smartframework.cloud.starter.rpc.feign.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;

/**
 * feign切面日志Dto
 *
 * @author collin
 * @date 2019-04-09
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@JsonPropertyOrder({"classMethod", "cost", "params", "headers", "result"})
public class FeignLogAspectDO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 调用的类方法
     */
    private String classMethod;

    /**
     * 请求处理时间,毫秒
     */
    private Long cost;

    /**
     * 请求的参数信息
     */
    private Object params;

    /**
     * 请求头
     */
    private Object headers;

    /**
     * 响应数据
     */
    private Object result;

}