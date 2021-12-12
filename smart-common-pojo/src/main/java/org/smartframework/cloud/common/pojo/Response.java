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
package org.smartframework.cloud.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;

/**
 * 响应对象
 *
 * @author collin
 * @date 2020-05-07
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 响应头部
     */
    private ResponseHead head = null;

    /**
     * 响应体
     */
    private T body;

    /**
     * 签名
     */
    private String sign;

    public Response(ResponseHead head) {
        this.head = head;
    }

    public Response(T body) {
        this.head = new ResponseHead(CommonReturnCodes.SUCCESS);
        this.body = body;
    }

}