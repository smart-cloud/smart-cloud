package org.smartframework.cloud.exception;

import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 服务器异常
 *
 * @author liyulin
 * @date 2019-06-29
 */
public class ServerException extends BaseException {

    private static final long serialVersionUID = 1L;

    public ServerException(IBaseReturnCodes baseReturnCode) {
        super(baseReturnCode);
    }

    public ServerException(String message) {
        super.setCode(CommonReturnCodes.SERVER_ERROR.getCode());
        super.setMessage(message);
    }

}