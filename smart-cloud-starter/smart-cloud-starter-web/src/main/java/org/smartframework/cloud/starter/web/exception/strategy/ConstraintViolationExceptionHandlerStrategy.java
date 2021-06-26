package org.smartframework.cloud.starter.web.exception.strategy;

import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.web.exception.AbstractExceptionHandlerStrategy;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author liyulin
 * @desc 参数校验异常转换
 * @date 2019/10/29
 */
public class ConstraintViolationExceptionHandlerStrategy extends AbstractExceptionHandlerStrategy {

    @Override
    public boolean match(Throwable e) {
        return e instanceof ConstraintViolationException;
    }

    @Override
    public ResponseHead transRespHead(Throwable e) {
        // 参数校验
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
        Set<ConstraintViolation<?>> constraintViolationSet = constraintViolationException.getConstraintViolations();
        if (CollectionUtils.isNotEmpty(constraintViolationSet)) {
            String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSet);
            return RespHeadUtil.of(CommonReturnCodes.VALIDATE_FAIL, errorMsg);
        }

        return RespHeadUtil.of(CommonReturnCodes.VALIDATE_FAIL, e.getMessage());
    }

}