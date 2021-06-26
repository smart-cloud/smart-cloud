package org.smartframework.cloud.starter.web.exception;

import org.smartframework.cloud.common.pojo.ResponseHead;

/**
 * @author liyulin
 * @desc 接口异常转换
 * @date 2019/10/29
 */
public abstract class AbstractExceptionHandlerStrategy {

    public boolean isNeedServletEnv() {
        return false;
    }

    /**
     * 异常类型匹配
     *
     * @param e
     * @return
     */
    public abstract boolean match(Throwable e);

    /**
     * 将异常转化为响应体
     *
     * @param e
     * @return
     */
    public abstract ResponseHead transRespHead(Throwable e);

}