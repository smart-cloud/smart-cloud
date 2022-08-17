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
package io.github.smart.cloud.starter.redis.intercept;

import io.github.smart.cloud.constants.SymbolConstant;
import io.github.smart.cloud.utility.security.Md5Util;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * redis拦截器父类
 *
 * @author collin
 * @date 2022-03-11
 */
@RequiredArgsConstructor
public abstract class AbstractRedisInterceptor implements MethodInterceptor {

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    /**
     * md5长度
     */
    private static final int MD5_LENGTH = 32;
    protected final RedissonClient redissonClient;

    /**
     * 获取缓存key前缀
     *
     * @param prefix
     * @param name
     * @param method
     * @return
     */
    protected StringBuilder getPrefix(String prefix, String name, Method method) {
        StringBuilder key = new StringBuilder(32);
        key.append(prefix);
        if (StringUtils.isBlank(name)) {
            key.append(method.getDeclaringClass().getSimpleName().toLowerCase());
            key.append(SymbolConstant.COLON);
            key.append(method.getName().toLowerCase());
            key.append(SymbolConstant.COLON);
        } else {
            key.append(name);
            if (!name.endsWith(SymbolConstant.COLON)) {
                key.append(SymbolConstant.COLON);
            }
        }

        return key;
    }

    /**
     * 获取缓存key后缀
     *
     * @param expressions
     * @param method
     * @param arguments
     * @return
     */
    protected StringBuilder getSuffix(String[] expressions, Method method, Object[] arguments) {
        EvaluationContext evaluationContext = getEvaluationContext(method, arguments);
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < expressions.length; i++) {
            if (i > 0) {
                key.append(SymbolConstant.COLON);
            }
            key.append(PARSER.parseExpression(expressions[i]).getValue(evaluationContext));
        }

        return key;
    }

    /**
     * 获取key
     *
     * @param keyPrefix
     * @param expressions
     * @param method
     * @param arguments
     * @return
     */
    protected String getKey(String prefixType, String keyPrefix, String[] expressions, Method method, Object[] arguments) {
        String finalKeyPrefix = getPrefix(prefixType, keyPrefix, method).toString();
        String keySuffix = getSuffix(expressions, method, arguments).toString();
        String finalKeySuffix = keySuffix.length() > MD5_LENGTH ? Md5Util.md5Hex(keySuffix) : keySuffix;
        return finalKeyPrefix + finalKeySuffix;
    }

    /**
     * 获取EvaluationContext对象
     *
     * @param method
     * @param arguments
     * @return
     */
    protected final EvaluationContext getEvaluationContext(Method method, Object[] arguments) {
        String[] parameterNames = DISCOVERER.getParameterNames(method);
        EvaluationContext evaluationContext = new StandardEvaluationContext(method);
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                evaluationContext.setVariable(parameterNames[i], arguments[i]);
            }
        }
        return evaluationContext;
    }

}