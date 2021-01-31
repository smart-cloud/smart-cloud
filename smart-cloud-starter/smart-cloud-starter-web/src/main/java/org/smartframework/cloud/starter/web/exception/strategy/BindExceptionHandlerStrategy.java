package org.smartframework.cloud.starter.web.exception.strategy;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.AbstractExceptionHandlerStrategy;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author liyulin
 * @desc 参数检验异常转换
 * @date 2019/10/29
 */
public class BindExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof BindException;
    }

    @Override
    public RespHeadVO transRespHead(Throwable e) {
        // 参数校验
        BindException bindException = (BindException) e;
        List<FieldError> fieldErrors = bindException.getFieldErrors();
        if (CollectionUtils.isNotEmpty(fieldErrors)) {
            String errorMsg = ExceptionUtil.getErrorMsg(fieldErrors);
            return RespHeadUtil.of(CommonReturnCodes.VALIDATE_FAIL, errorMsg);
        }
        return null;
    }

}