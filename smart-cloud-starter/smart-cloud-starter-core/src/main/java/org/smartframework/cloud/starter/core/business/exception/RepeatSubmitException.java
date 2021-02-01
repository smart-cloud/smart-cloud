package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 重复提交校验异常
 *
 * @author liyulin
 * @date 2019-07-03
 */
public class RepeatSubmitException extends BaseException {

    private static final long serialVersionUID = 1L;

    public RepeatSubmitException() {
        setCode(CommonReturnCodes.REPEAT_SUBMIT.getCode());
    }

    public RepeatSubmitException(IBaseReturnCodes returnCodes) {
        setCode(returnCodes.getCode());
    }

}