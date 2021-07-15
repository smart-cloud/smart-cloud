package org.smartframework.cloud.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * RPC请求失败异常
 * @author collin
 * @date 2021-07-15
 */
public class RpcException extends BaseException {

    public RpcException(IBaseReturnCodes baseReturnCode) {
        super(baseReturnCode);
    }

    public RpcException(String message) {
        super.setCode(CommonReturnCodes.RPC_REQUEST_FAIL.getCode());
        super.setMessage(message);
    }

}