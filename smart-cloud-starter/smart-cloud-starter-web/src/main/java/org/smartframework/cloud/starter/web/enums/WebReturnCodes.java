package org.smartframework.cloud.starter.web.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * starter-web模块返回码
 *
 * @author collin
 * @date 2021-10-31
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WebReturnCodes implements IBaseReturnCodes {

    /**
     * 待校验参数object不能为null
     */
    VALIDATE_IN_PARAMS_NULL("2001");

    /**
     * 状态码
     */
    private String code;

}