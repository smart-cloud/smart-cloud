/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.exception;

/**
 * 自定义异常基类
 *
 * @author collin
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
    /**
     * 提示信息参数
     */
    private Object[] args;

    protected BaseException() {
    }

    protected BaseException(String code) {
        this.code = code;
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

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "[" + code + "]:" + message;
    }

}