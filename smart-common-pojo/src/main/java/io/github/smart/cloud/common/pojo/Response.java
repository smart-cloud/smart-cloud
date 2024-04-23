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

import io.github.smart.cloud.constants.CommonReturnCodes;
import lombok.*;
import uk.co.jemos.podam.common.PodamStringValue;

import java.io.Serializable;

/**
 * 响应对象
 *
 * @author collin
 * @date 2020-05-07
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 全局唯一交易流水号
     */
    private String nonce;

    /**
     * 响应状态码
     */
    @PodamStringValue(strValue = CommonReturnCodes.SUCCESS)
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应体
     */
    private T body;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    /**
     * 签名
     */
    private String sign;

    public Response(String code) {
        this.code = code;
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(T body) {
        this(CommonReturnCodes.SUCCESS, null);
        this.body = body;
    }

}