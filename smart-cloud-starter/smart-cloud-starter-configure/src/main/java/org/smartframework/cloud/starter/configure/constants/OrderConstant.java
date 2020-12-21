package org.smartframework.cloud.starter.configure.constants;

/**
 * bean执行顺序
 *
 * @author liyulin
 * @date 2019-06-28
 */
public interface OrderConstant {

    /**
     * http filter
     */
    int HTTP_FITLER = Integer.MIN_VALUE;
    /**
     * 接口日志
     */
    int API_LOG = 1;
    /**
     * 多语言切面
     */
    int LOCALE = 2;

}