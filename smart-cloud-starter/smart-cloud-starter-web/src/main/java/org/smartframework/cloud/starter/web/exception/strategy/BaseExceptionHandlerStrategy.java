package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.core.business.exception.BaseException;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.AbstractExceptionHandlerStrategy;
import org.smartframework.cloud.utility.spring.I18NUtil;

/**
 * @author liyulin
 * @desc 自定义异常转换
 * @date 2019/10/29
 */
public class BaseExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof BaseException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        BaseException ex = (BaseException) e;
        return RespHeadUtil.of(ex.getCode(), I18NUtil.getMessage(ex.getMessage()));
    }

}