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
package io.github.smart.cloud.starter.mybatis.plus.plugin;

import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;

/**
 * mybatis sql日志打印
 *
 * @author collin
 * @date 2019-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}), @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class})})
public class MybatisSqlLogInterceptor implements Interceptor {

    private static final String QUOTE = "\\?";
    /**
     * 参数数组的长度
     */
    private static final int ARGS_LENGTH = 6;
    /**
     * 默认日志最大长度
     */
    private static final int DEFAULT_LOG_MAX_LENGTH = 2048;
    /**
     * 日志级别
     */
    private final SmartProperties smartProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = null;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
            } finally {
                if (log.isWarnEnabled()) {
                    try {
                        long end = System.currentTimeMillis();
                        long time = (end - start);
                        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                        BoundSql boundSql;
                        if (invocation.getArgs().length == ARGS_LENGTH) {
                            boundSql = (BoundSql) invocation.getArgs()[ARGS_LENGTH - 1];
                        } else {
                            Object parameter = invocation.getArgs()[1];
                            boundSql = mappedStatement.getBoundSql(parameter);
                        }
                        showSql(mappedStatement.getConfiguration(), boundSql, mappedStatement.getId(), time, returnValue);
                    } catch (Throwable logException) {
                        // 日志格式化失败不能覆盖原始 SQL 业务异常。
                        log.warn("mybatis sql log failed", logException);
                    }
                }
            }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        // 只对要拦截的对象生成代理
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * sql日志拼接
     *
     * <p>
     * 不能用换行。如果使用换行，在ELK中日志的顺序将会混乱
     *
     * @param configuration
     * @param boundSql
     * @param sqlId
     * @param time
     * @param returnValue
     */
    public void showSql(Configuration configuration, BoundSql boundSql, String sqlId, long time, Object returnValue) {
        String separator = "==>";
        StringBuilder str = new StringBuilder(64);
        String shortSqlId = getShortSqlId(sqlId);
        str.append(shortSqlId)
                .append(':');
        Object parameterObject = boundSql.getParameterObject();
        // 过滤掉第三方定义的对象，避免循环引用时序列化报错
        if (canMask(parameterObject)) {
            String sql = cleanSql(boundSql.getSql());
            str.append(sql)
                    .append(separator)
                    .append(JacksonUtil.toJson(parameterObject));
        } else {
            String sql = getSql(configuration, boundSql);
            str.append(sql);
        }
        str.append(separator)
                .append("spend:")
                .append(time)
                .append("ms")
                .append(separator)
                .append("result")
                .append(separator)
                .append(JacksonUtil.toJson(returnValue));

        if (log.isInfoEnabled()) {
            int maxLength = smartProperties.getMybatis().getLogMaxLength() == null ? DEFAULT_LOG_MAX_LENGTH : smartProperties.getMybatis().getLogMaxLength();
            log.info(StringUtils.truncate(str.toString(), maxLength));
        }
    }

    private boolean canMask(Object object) {
        return object instanceof Serializable && !(object instanceof Map);
    }

    /**
     * 截取sqlId（只保留class.method）
     *
     * @param sqlId
     * @return
     */
    public String getShortSqlId(String sqlId) {
        for (int i = sqlId.length() - 1, times = 0; i >= 0; i--) {
            if (sqlId.charAt(i) == '.' && (++times) == 2) {
                return sqlId.substring(i + 1);
            }
        }

        return sqlId;
    }

    private String getParameterValue(Object obj) {
        String params = "";
        if (obj instanceof String) {
            params = "'" + obj + "'";
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            params = "'" + DateUtil.formatDateTime(date) + "'";
        } else if (Objects.isNull(obj)) {
            params = "null";
        } else {
            params = obj.toString();
        }

        return Matcher.quoteReplacement(params);
    }

    private String getSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql = cleanSql(boundSql.getSql());
        if (CollectionUtils.isEmpty(parameterMappings) || parameterObject == null) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            sql = sql.replaceFirst(QUOTE, getParameterValue(parameterObject));
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst(QUOTE, getParameterValue(obj));
                } else if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst(QUOTE, getParameterValue(obj));
                }
            }
        }

        return sql;
    }

    private String cleanSql(String sql) {
        return sql.replaceAll("[\\s]+", " ");
    }

}