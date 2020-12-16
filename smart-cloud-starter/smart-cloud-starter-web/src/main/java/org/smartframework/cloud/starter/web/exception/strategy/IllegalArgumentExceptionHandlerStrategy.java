package org.smartframework.cloud.starter.web.exception.strategy;

import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.AbstractExceptionHandlerStrategy;

/**
 * @author liyulin
 * @desc 参数不合法异常转换
 * @date 2019/10/29
 */
public class IllegalArgumentExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof IllegalArgumentException;
    }

    @Override
    public RespHeadVO transRespHead(Throwable e) {
        return RespHeadUtil.ofI18n(ReturnCodeEnum.VALIDATE_FAIL);
    }

}