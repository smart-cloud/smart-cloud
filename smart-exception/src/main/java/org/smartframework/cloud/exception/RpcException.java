package org.smartframework.cloud.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;

/**
 * RPC请求失败异常
 *
 * @author collin
 * @date 2021-07-15
 */
public class RpcException extends BaseException {

    public RpcException() {
        super(CommonReturnCodes.RPC_REQUEST_FAIL);
    }

    public RpcException(String message) {
        super.setCode(CommonReturnCodes.RPC_REQUEST_FAIL.getCode());
        super.setMessage(message);
    }

}