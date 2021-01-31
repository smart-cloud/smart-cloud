package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 业务异常
 *
 * @author liyulin
 * @date 2019-06-29
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IBaseReturnCodes baseReturnCode) {
        super(baseReturnCode);
    }

}