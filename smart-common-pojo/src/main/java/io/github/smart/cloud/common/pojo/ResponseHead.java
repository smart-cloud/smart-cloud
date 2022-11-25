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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import io.github.smart.cloud.constants.CommonReturnCodes;
import uk.co.jemos.podam.common.PodamStringValue;

import java.io.Serializable;

/**
 * 响应头部
 *
 * @author collin
 * @date 2020-05-07
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public class ResponseHead implements Serializable {

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
     * 响应时间戳
     */
    private long timestamp;

    public ResponseHead(String code) {
        this.code = code;
    }

    public ResponseHead(String code, String message) {
        this.code = code;
        this.message = message;
    }

}