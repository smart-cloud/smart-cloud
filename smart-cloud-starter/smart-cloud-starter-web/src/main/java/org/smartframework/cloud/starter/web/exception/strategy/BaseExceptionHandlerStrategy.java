package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.exception.BaseException;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.smartframework.cloud.utility.spring.I18nUtil;

/**
 * @author liyulin
 * @desc 自定义异常转换
 * @date 2019/10/29
 */
public class BaseExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof BaseException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        BaseException ex = (BaseException) e;
        return RespHeadUtil.of(ex.getCode(), I18nUtil.getMessage(ex.getMessage()));
    }

}