package org.smartframework.cloud.starter.mybatis.plugin;

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
import org.smartframework.cloud.mask.util.MaskUtil;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.utility.DateUtil;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;

/**
 * mybatis sql日志打印
 *
 * @author liyulin
 * @date 2019-03-22
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class,
                RowBounds.class})})
@Slf4j
public class MybatisSqlLogInterceptor implements Interceptor {

    private static final String QUOTE = "\\?";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = null;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
            return returnValue;
        } finally {
            long end = System.currentTimeMillis();
            long time = (end - start);
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            BoundSql boundSql = null;
            if (invocation.getArgs().length == 6) {
                boundSql = (BoundSql) invocation.getArgs()[5];
            } else {
                Object parameter = invocation.getArgs()[1];
                boundSql = mappedStatement.getBoundSql(parameter);
            }
            String sqlId = mappedStatement.getId();
            Configuration configuration = mappedStatement.getConfiguration();
            showSql(configuration, boundSql, sqlId, time, returnValue);
        }
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
    public static void showSql(Configuration configuration, BoundSql boundSql, String sqlId, long time,
                               Object returnValue) {
        String separator = "==>";
        StringBuilder str = new StringBuilder(64);
        String shortSqlId = getShortSqlId(sqlId);
        str.append(shortSqlId);
        str.append("：");
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
        str.append("spend：");
        str.append(time);
        str.append("ms");
        str.append(separator);
        str.append("result");
        str.append(separator);
        str.append(MaskUtil.mask(returnValue));

        log.info(LogUtil.truncate(str.toString()));
    }

    private static boolean canMask(Object object) {
        return object != null && object instanceof Serializable && !(object instanceof Map);
    }

    /**
     * 截取sqlId（只保留class.method）
     *
     * @param sqlId
     * @return
     */
    public static String getShortSqlId(String sqlId) {
        for (int i = sqlId.length() - 1, times = 0; i >= 0; i--) {
            if (sqlId.charAt(i) == '.' && (++times) == 2) {
                return sqlId.substring(i + 1);
            }
        }

        return sqlId;
    }

    private static String getParameterValue(Object obj) {
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

    public static String getSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql = cleanSql(boundSql.getSql());
        if (CollectionUtils.isEmpty(parameterMappings) || Objects.isNull(parameterObject)) {
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

    private static String cleanSql(String sql) {
        return sql.replaceAll("[\\s]+", " ");
    }

}