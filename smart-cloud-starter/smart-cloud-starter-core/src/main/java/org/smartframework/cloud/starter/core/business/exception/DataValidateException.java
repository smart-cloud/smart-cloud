package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 数据校验错误
 *
 * @author liyulin
 * @date 2019-05-01
 */
public class DataValidateException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DataValidateException() {
        setCode(CommonReturnCodes.DATA_MISSING.getCode());
    }

    public DataValidateException(IBaseReturnCodes returnCodes) {
        setCode(returnCodes.getCode());
    }

}