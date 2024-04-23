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
package io.github.smart.cloud.starter.core.business.util;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.constants.CommonReturnCodes;
import io.github.smart.cloud.utility.NonceUtil;
import io.github.smart.cloud.utility.spring.I18nUtil;

/**
 * {@link Response}工具类
 *
 * @author collin
 * @date 2019-04-06
 */
public class ResponseUtil {

    private ResponseUtil() {
    }

    /**
     * 构造响应成功对象
     *
     * @return
     */
    public static <R> Response<R> success() {
        return new Response<>(CommonReturnCodes.SUCCESS);
    }

    /**
     * 构造响应成功对象
     *
     * @param data
     * @return
     */
    public static <R> Response<R> success(R data) {
        return new Response<>(data);
    }

    /**
     * 构造响应错误对象
     *
     * @param code
     * @return
     */
    public static <R> Response<R> error(String code) {
        return new Response<>(code);
    }

    /**
     * 构造response对象
     *
     * @param code
     * @return
     */
    public static Response of(String code, String message) {
        Response response = new Response(code, message);
        response.setTimestamp(System.currentTimeMillis());
        response.setNonce(String.valueOf(NonceUtil.nextId()));
        return response;
    }

    /**
     * 构造response对象
     *
     * @param code
     * @return
     */
    public static Response ofI18n(String code) {
        Response response = new Response(code);
        response.setTimestamp(System.currentTimeMillis());
        response.setNonce(String.valueOf(NonceUtil.nextId()));
        response.setMessage(I18nUtil.getMessage(code));
        return response;
    }

    /**
     * 是否成功
     *
     * @param response
     * @return
     */
    public static <R> boolean isSuccess(Response<R> response) {
        return response != null && CommonReturnCodes.SUCCESS.equals(response.getCode());
    }

    /**
     * 获取失败的提示信息
     *
     * @param response
     * @return
     */
    public static <R> String getFailMsg(Response<R> response) {
        if (response == null) {
            return I18nUtil.getMessage(CommonReturnCodes.RPC_REQUEST_FAIL);
        }

        return response.getMessage();
    }

}