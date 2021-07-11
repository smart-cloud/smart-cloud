package org.smartframework.cloud.starter.web.exception;

import org.smartframework.cloud.common.pojo.ResponseHead;

/**
 * 接口异常转换
 *
 * @author liyulin
 * @date 2019/10/29
 */
public interface IExceptionHandlerStrategy {

    default boolean isNeedServletEnv() {
        return false;
    }

    /**
     * 异常类型匹配
     *
     * @param e
     * @return
     */
    boolean match(Throwable e);

    /**
     * 将异常转化为响应体
     *
     * @param e
     * @return
     */
    ResponseHead transRespHead(Throwable e);

}