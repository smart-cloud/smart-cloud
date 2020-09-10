package org.smartframework.cloud.starter.test.core;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author liyulin
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

}