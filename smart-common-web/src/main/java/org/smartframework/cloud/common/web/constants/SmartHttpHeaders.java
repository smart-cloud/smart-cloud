package org.smartframework.cloud.common.web.constants;

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