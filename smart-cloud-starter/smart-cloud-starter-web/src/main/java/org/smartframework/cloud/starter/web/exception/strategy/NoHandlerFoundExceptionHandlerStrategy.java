package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.AbstractExceptionHandlerStrategy;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author liyulin
 * @desc mapping url找不到异常转换
 * @date 2019/10/29
 */
public class NoHandlerFoundExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy {

    @Override
    public boolean isNeedServletEnv() {
        return true;
    }

    @Override
    public boolean match(Throwable e) {
        return e instanceof NoHandlerFoundException;
    }

    @Override
    public RespHeadVO transRespHead(Throwable e) {
        return RespHeadUtil.ofI18n(ReturnCodeEnum.REQUEST_URL_ERROR);
    }

}