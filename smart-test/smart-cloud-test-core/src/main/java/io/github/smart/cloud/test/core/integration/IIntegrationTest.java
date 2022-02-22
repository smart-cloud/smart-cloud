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
package io.github.smart.cloud.test.core.integration;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

/**
 * @author collin
 * @date 2020-09-07
 */
public interface IIntegrationTest {

    /**
     * post请求
     *
     * @param url           请求mapping的地址
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    <T> T post(String url, Object req, TypeReference<T> typeReference) throws Exception;

    /**
     * post请求
     *
     * @param url           请求mapping的地址
     * @param headers       请求header参数
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    <T> T post(String url, Map<String, String> headers, Object req, TypeReference<T> typeReference) throws Exception;

    /**
     * get请求
     *
     * @param url           请求mapping的地址
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    <T> T get(String url, Object req, TypeReference<T> typeReference)
            throws Exception;

    /**
     * get请求
     *
     * @param url           请求mapping的地址
     * @param headers       请求header参数
     * @param req           请求参数
     * @param typeReference 返回对象类型
     * @return
     * @throws Exception
     */
    <T> T get(String url, Map<String, String> headers, Object req, TypeReference<T> typeReference)
            throws Exception;

}