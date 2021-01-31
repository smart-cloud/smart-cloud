package org.smartframework.cloud.starter.core.business.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CoreReturnCodes implements IBaseReturnCodes {

    /**
     * rpc请求失败
     */
    RPC_REQUEST_FAIL("1001"),
    /**
     * rpc返回结果异常
     */
    RPC_RESULT_EXCEPTION("1002");

    /**
     * 状态码
     */
    private String code;

}