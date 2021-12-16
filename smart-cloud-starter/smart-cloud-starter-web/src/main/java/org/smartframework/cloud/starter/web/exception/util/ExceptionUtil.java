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
package org.smartframework.cloud.starter.web.exception.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.constants.SymbolConstant;
import org.smartframework.cloud.utility.spring.I18nUtil;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

/**
 * 异常工具类
 *
 * @author collin
 * @date 2019-04-09
 */
public class ExceptionUtil {

    /**
     * 错误分隔符
     */
    private static final String ERROR_SEPARATOR = " | ";

    private ExceptionUtil() {
    }

    /**
     * 异常信息序列化
     *
     * @param t
     * @return
     */
    public static String toString(Throwable t) {
        StackTraceElement[] stackTraceElements = t.getStackTrace();
        return t.getClass().getTypeName() + ERROR_SEPARATOR + StringUtils.join(stackTraceElements, ERROR_SEPARATOR);
    }

    public static String getErrorMsg(Set<ConstraintViolation<?>> constraintViolationSet) {
        StringBuilder errorMsg = new StringBuilder();
        int size = constraintViolationSet.size();
        int i = 0;
        for (ConstraintViolation<?> constraintViolation : constraintViolationSet) {
            if (size > 1) {
                errorMsg.append((++i) + SymbolConstant.DOT);
            }
            if (constraintViolation.getPropertyPath() == null) {
                errorMsg.append(constraintViolation.getMessage());
            } else {
                errorMsg.append(constraintViolation.getPropertyPath().toString()).append(SymbolConstant.HYPHEN)
                        .append(I18nUtil.getMessage(constraintViolation.getMessage()));
            }
            if (size > 1 && i < size) {
                errorMsg.append("; ");
            }
        }

        return errorMsg.toString();
    }

    public static String getErrorMsg(List<FieldError> fieldErrors) {
        StringBuilder errorMsg = new StringBuilder();
        for (int i = 0, size = fieldErrors.size(); i < size; i++) {
            if (size > 1) {
                errorMsg.append((i + 1) + SymbolConstant.DOT);
            }

            String validateField = fieldErrors.get(i).getField();
            errorMsg.append(validateField + SymbolConstant.HYPHEN + I18nUtil.getMessage(fieldErrors.get(i).getDefaultMessage()));
            if (size > 1 && i < size - 1) {
                errorMsg.append("; ");
            }
        }

        return errorMsg.toString();
    }

}