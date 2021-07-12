package org.smartframework.cloud.starter.web.validation.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.smartframework.cloud.exception.ParamValidateException;
import org.smartframework.cloud.starter.web.enums.WebReturnCodes;
import org.smartframework.cloud.starter.web.exception.util.ExceptionUtil;
import org.smartframework.cloud.starter.web.validation.ValidatorSingleton;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验
 *
 * @author liyulin
 * @date 2019-05-01
 */
@UtilityClass
public class ValidationUtil {

    /**
     * 参数校验
     *
     * @param object
     */
    public static void validate(Object object) {
        if (object == null) {
            throw new ParamValidateException(WebReturnCodes.VALIDATE_IN_PARAMS_NULL);
        }

        Validator validator = ValidatorSingleton.getInstance();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(object);
        // 抛出检验异常
        if (CollectionUtils.isNotEmpty(constraintViolationSet)) {
            Set<ConstraintViolation<?>> constraintViolationSetTmp = constraintViolationSet.stream()
                    .map(item -> (ConstraintViolation<?>) (item)).collect(Collectors.toSet());

            String errorMsg = ExceptionUtil.getErrorMsg(constraintViolationSetTmp);
            throw new ParamValidateException(errorMsg);
        }
    }

}