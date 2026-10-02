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

import io.github.smart.cloud.starter.configure.properties.MybatisProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.utility.DateUtil;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * mybatis sql日志打印
 *
 * <p>
 * 拦截StatementHandler而不是Executor：直接复用StatementHandler中已生成的BoundSql，避免重复执行动态sql解析；
 * 此时sql已经过分页、多租户等插件改写，打印的是最终执行的sql，与拦截器顺序无关。
 * <p>
 * 注意：耗时只统计语句执行及结果处理（不含获取连接、预编译）；命中一、二级缓存的查询不会执行sql，因此不会打印。
 *
 * @author collin
 * @date 2019-03-22
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "queryCursor", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class MybatisSqlLogInterceptor implements Interceptor {

    private static final String SEPARATOR = "==>";
    private static final char PLACEHOLDER = '?';
    /**
     * 截断标识（与org.springframework.util.StringUtils#truncate保持一致）
     */
    private static final String TRUNCATED_SUFFIX = " (truncated)...";
    /**
     * 默认日志最大长度
     */
    private static final int DEFAULT_LOG_MAX_LENGTH = 2048;
    /**
     * StringBuilder初始容量上限
     */
    private static final int MAX_INITIAL_CAPACITY = 512;
    /**
     * mybatis插件代理的目标对象字段
     */
    private static final Field PLUGIN_TARGET_FIELD = accessibleField(Plugin.class, "target");
    /**
     * RoutingStatementHandler实际委托的StatementHandler字段
     */
    private static final Field ROUTING_DELEGATE_FIELD = accessibleField(RoutingStatementHandler.class, "delegate");
    /**
     * BaseStatementHandler的MappedStatement字段
     */
    private static final Field MAPPED_STATEMENT_FIELD = accessibleField(BaseStatementHandler.class, "mappedStatement");
    /**
     * 日志级别
     */
    private final SmartProperties smartProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MybatisProperties mybatisProperties = smartProperties.getMybatis();
        // 普通sql以info级别输出，慢sql以warn级别输出；都不需要输出时直接放行，避免拼接sql、序列化参数和结果的开销
        boolean infoEnabled = !mybatisProperties.isOnlySlowSql() && log.isInfoEnabled();
        if (!infoEnabled && !log.isWarnEnabled()) {
            return invocation.proceed();
        }

        Object returnValue = null;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
        } finally {
            long time = System.currentTimeMillis() - start;
            boolean slow = time >= mybatisProperties.getSlowSqlMinCost();
            if (slow ? log.isWarnEnabled() : infoEnabled) {
                try {
                    logSql((StatementHandler) invocation.getTarget(), time, returnValue, slow, mybatisProperties);
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
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private void logSql(StatementHandler statementHandler, long time, Object returnValue, boolean slow, MybatisProperties mybatisProperties) {
        MappedStatement mappedStatement = getMappedStatement(statementHandler);
        if (mappedStatement == null) {
            return;
        }

        String content = buildSqlLog(mappedStatement.getConfiguration(), statementHandler.getBoundSql(), mappedStatement.getId(), time, returnValue, mybatisProperties);
        if (slow) {
            log.warn(content);
        } else {
            log.info(content);
        }
    }

    /**
     * sql日志拼接
     *
     * <p>
     * 不能用换行。如果使用换行，在ELK中日志的顺序将会混乱
     * <p>
     * 拼接过程中一旦超过日志最大长度即停止拼接（大结果集序列化也会提前中止），避免无效的拼接和序列化开销
     *
     * @param configuration
     * @param boundSql
     * @param sqlId
     * @param time
     * @param returnValue
     * @param mybatisProperties
     * @return
     */
    private String buildSqlLog(Configuration configuration, BoundSql boundSql, String sqlId, long time, Object returnValue, MybatisProperties mybatisProperties) {
        Integer configMaxLength = mybatisProperties.getLogMaxLength();
        int maxLength = (configMaxLength == null || configMaxLength <= 0) ? DEFAULT_LOG_MAX_LENGTH : configMaxLength;
        // 多拼接1个字符，用于判断是否需要截断
        int limit = maxLength + 1;

        StringBuilder str = new StringBuilder(Math.min(limit, MAX_INITIAL_CAPACITY));
        str.append(sqlId, shortSqlIdStart(sqlId), sqlId.length())
                .append(':');
        Object parameterObject = boundSql.getParameterObject();
        // 过滤掉第三方定义的对象，避免循环引用时序列化报错
        if (canMask(parameterObject)) {
            appendCleanSql(str, boundSql.getSql(), limit);
            str.append(SEPARATOR);
            appendJson(str, parameterObject, limit);
        } else {
            appendSql(str, configuration, boundSql, limit);
        }

        if (str.length() < limit) {
            str.append(SEPARATOR)
                    .append("spend:")
                    .append(time)
                    .append("ms");
            if (mybatisProperties.isLogResult()) {
                str.append(SEPARATOR)
                        .append("result")
                        .append(SEPARATOR);
                appendJson(str, returnValue, limit);
            }
        }

        // 原地截断，避免subSequence + 字符串拼接的额外复制
        if (str.length() > maxLength) {
            str.setLength(maxLength);
            str.append(TRUNCATED_SUFFIX);
        }
        return str.toString();
    }

    private boolean canMask(Object object) {
        return object instanceof Serializable && !(object instanceof Map);
    }

    /**
     * 获取短sqlId（class.method）在sqlId中的起始下标
     *
     * @param sqlId
     * @return
     */
    private static int shortSqlIdStart(String sqlId) {
        for (int i = sqlId.length() - 1, times = 0; i >= 0; i--) {
            if (sqlId.charAt(i) == '.' && (++times) == 2) {
                return i + 1;
            }
        }

        return 0;
    }

    /**
     * 追加对象的json，超过剩余长度时提前中止序列化
     *
     * @param str
     * @param value
     * @param limit
     */
    private void appendJson(StringBuilder str, Object value, int limit) {
        int remaining = limit - str.length();
        if (remaining > 0) {
            str.append(JacksonUtil.toJson(value, remaining));
        }
    }

    private void appendParameterValue(StringBuilder str, Object obj, int limit) {
        if (obj instanceof String) {
            String value = (String) obj;
            str.append('\'');
            // 超长字符串参数只追加剩余长度部分
            int remaining = limit - str.length();
            if (value.length() > remaining) {
                str.append(value, 0, Math.max(remaining, 0));
            } else {
                str.append(value).append('\'');
            }
        } else if (obj instanceof Date) {
            str.append('\'').append(DateUtil.formatDateTime((Date) obj)).append('\'');
        } else {
            // null时追加"null"
            str.append(obj);
        }
    }

    /**
     * 追加参数替换后的sql。单次遍历完成空白字符压缩和占位符替换，替代多次正则replaceFirst
     *
     * @param str
     * @param configuration
     * @param boundSql
     * @param limit
     */
    private void appendSql(StringBuilder str, Configuration configuration, BoundSql boundSql, int limit) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql();
        if (CollectionUtils.isEmpty(parameterMappings) || parameterObject == null) {
            appendCleanSql(str, sql, limit);
            return;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        boolean simpleParameter = typeHandlerRegistry.hasTypeHandler(parameterObject.getClass());
        MetaObject metaObject = null;
        int parameterIndex = 0;
        int parameterSize = parameterMappings.size();
        boolean lastWhitespace = false;
        for (int i = 0, length = sql.length(); i < length && str.length() < limit; i++) {
            char c = sql.charAt(i);
            if (isWhitespace(c)) {
                if (!lastWhitespace) {
                    str.append(' ');
                    lastWhitespace = true;
                }
                continue;
            }
            lastWhitespace = false;

            if (c != PLACEHOLDER || parameterIndex >= parameterSize) {
                str.append(c);
                continue;
            }

            // 简单类型参数只替换第一个占位符（与原有逻辑保持一致）
            if (simpleParameter) {
                if (parameterIndex++ == 0) {
                    appendParameterValue(str, parameterObject, limit);
                } else {
                    str.append(c);
                }
                continue;
            }

            String propertyName = parameterMappings.get(parameterIndex++).getProperty();
            if (boundSql.hasAdditionalParameter(propertyName)) {
                appendParameterValue(str, boundSql.getAdditionalParameter(propertyName), limit);
                continue;
            }

            if (metaObject == null) {
                metaObject = configuration.newMetaObject(parameterObject);
            }
            if (metaObject.hasGetter(propertyName)) {
                appendParameterValue(str, metaObject.getValue(propertyName), limit);
            } else {
                // 取不到参数值时保留占位符
                str.append(c);
            }
        }
    }

    /**
     * 追加压缩空白字符后的sql（连续空白字符替换为一个空格）
     *
     * @param str
     * @param sql
     * @param limit
     */
    private void appendCleanSql(StringBuilder str, String sql, int limit) {
        boolean lastWhitespace = false;
        for (int i = 0, length = sql.length(); i < length && str.length() < limit; i++) {
            char c = sql.charAt(i);
            if (isWhitespace(c)) {
                if (!lastWhitespace) {
                    str.append(' ');
                    lastWhitespace = true;
                }
            } else {
                str.append(c);
                lastWhitespace = false;
            }
        }
    }

    /**
     * 与正则\s保持一致：[ \t\n\x0B\f\r]
     *
     * @param c
     * @return
     */
    private static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\u000B' || c == '\f' || c == '\r';
    }

    /**
     * 从StatementHandler中获取MappedStatement（反射字段已缓存）
     *
     * @param statementHandler
     * @return 获取不到时返回null
     */
    private static MappedStatement getMappedStatement(StatementHandler statementHandler) {
        Object target = realTarget(statementHandler);
        if (target instanceof RoutingStatementHandler && ROUTING_DELEGATE_FIELD != null) {
            target = ReflectionUtils.getField(ROUTING_DELEGATE_FIELD, target);
        }
        if (target instanceof BaseStatementHandler && MAPPED_STATEMENT_FIELD != null) {
            return (MappedStatement) ReflectionUtils.getField(MAPPED_STATEMENT_FIELD, target);
        }
        return null;
    }

    /**
     * 剥离mybatis插件代理，获取真实的目标对象
     *
     * @param target
     * @return
     */
    private static Object realTarget(Object target) {
        while (PLUGIN_TARGET_FIELD != null && Proxy.isProxyClass(target.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(target);
            if (!(handler instanceof Plugin)) {
                break;
            }
            target = ReflectionUtils.getField(PLUGIN_TARGET_FIELD, handler);
        }
        return target;
    }

    private static Field accessibleField(Class<?> clazz, String name) {
        try {
            Field field = ReflectionUtils.findField(clazz, name);
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
            }
            return field;
        } catch (RuntimeException e) {
            log.warn("mybatis sql log: field {}.{} is not accessible", clazz.getName(), name, e);
            return null;
        }
    }

}
