package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 参数校验错误
 *
 * @author liyulin
 * @date 2019-05-01
 */
public class ParamValidateException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ParamValidateException(IBaseReturnCodes returnCodes) {
        setCode(returnCodes.getCode());
    }

    public ParamValidateException(String message) {
        setCode(CommonReturnCodes.PARAMETERS_MISSING.getCode());
        setMessage(message);
    }

}