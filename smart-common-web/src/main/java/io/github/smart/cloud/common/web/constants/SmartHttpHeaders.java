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
package io.github.smart.cloud.common.web.constants;

/**
 * smart cloud http header参数常量
 *
 * @author collin
 * @date 2021-07-17
 */
public interface SmartHttpHeaders {

    /**
     * http header timestamp
     */
    String TIMESTAMP = "smart-timestamp";
    /**
     * http header nonce
     */
    String NONCE = "smart-nonce";
    /**
     * 请求token
     */
    String TOKEN = "smart-token";
    /**
     * 请求参数签名
     */
    String SIGN = "smart-sign";

}