package org.smartframework.cloud.starter.core.business.exception;

import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;

/**
 * 自定义异常基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    public BaseException() {
    }

    public BaseException(IBaseReturnCodes baseReturnCodes) {
        setCode(baseReturnCodes.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        // 如果为空，则从国际化配置里面根据code取
        return message == null ? code : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + code + "]:" + message;
    }

}