package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.IExceptionHandlerStrategy;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author liyulin
 * @desc mapping url找不到异常转换
 * @date 2019/10/29
 */
public class NoHandlerFoundExceptionHandlerStrategy implements IExceptionHandlerStrategy {

    @Override
    public boolean isNeedServletEnv() {
        return true;
    }

    @Override
    public boolean match(Throwable e) {
        return e instanceof NoHandlerFoundException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        return RespHeadUtil.ofI18n(CommonReturnCodes.REQUEST_URL_ERROR);
    }

}