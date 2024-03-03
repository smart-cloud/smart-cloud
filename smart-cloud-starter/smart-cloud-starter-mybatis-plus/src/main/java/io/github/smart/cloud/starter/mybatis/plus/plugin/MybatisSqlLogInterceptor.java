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

import io.github.smart.cloud.constants.LogLevel;
import io.github.smart.cloud.mask.util.LogUtil;
import io.github.smart.cloud.mask.util.MaskUtil;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.utility.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
                long end = System.currentTimeMillis();
                long time = (end - start);
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                BoundSql boundSql = null;
                if (invocation.getArgs().length == ARGS_LENGTH) {
                    boundSql = (BoundSql) invocation.getArgs()[ARGS_LENGTH - 1];
                } else {
                    Object parameter = invocation.getArgs()[1];
                    boundSql = mappedStatement.getBoundSql(parameter);
                }
                String sqlId = mappedStatement.getId();
                Configuration configuration = mappedStatement.getConfiguration();
                showSql(configuration, boundSql, sqlId, time, returnValue);
            }
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
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
        str.append(shortSqlId);
        str.append(":");
        Object parameterObject = boundSql.getParameterObject();
        // 过滤掉第三方定义的对象，避免循环引用时序列化报错
        if (canMask(parameterObject)) {
            String sql = cleanSql(boundSql.getSql());
            str.append(sql);
            str.append(separator);
            str.append(MaskUtil.mask(parameterObject));
        } else {
            String sql = getSql(configuration, boundSql);
            str.append(sql);
        }
        str.append(separator);
        str.append("spend:");
        str.append(time);
        str.append("ms");
        str.append(separator);
        str.append("result");
        str.append(separator);
        str.append(MaskUtil.mask(returnValue));

        String logLevel = smartProperties.getMybatis().getLogLevel();
        if (LogLevel.DEBUG.equals(logLevel) && log.isDebugEnabled()) {
            log.debug(LogUtil.truncate(str.toString()));
        } else if (LogLevel.INFO.equals(logLevel) && log.isInfoEnabled()) {
            log.info(LogUtil.truncate(str.toString()));
        } else if (LogLevel.WARN.equals(logLevel)) {
            log.warn(LogUtil.truncate(str.toString()));
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
                if (metaObject.hasGetter(propertyName)) {
                    Object obj = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst(QUOTE, getParameterValue(obj));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    Object obj = boundSql.getAdditionalParameter(propertyName);
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