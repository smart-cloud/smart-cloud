package org.smartframework.cloud.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;

/**
 * 获取锁失败异常
 *
 * @author collin
 * @date 2022-02-04
 */
public class AcquiredLockFailException extends BaseException {

    private static final long serialVersionUID = 1L;

    public AcquiredLockFailException() {
        super(CommonReturnCodes.GET_LOCK_FAIL);
    }

    public AcquiredLockFailException(String code) {
        super.setCode(code);
    }

}