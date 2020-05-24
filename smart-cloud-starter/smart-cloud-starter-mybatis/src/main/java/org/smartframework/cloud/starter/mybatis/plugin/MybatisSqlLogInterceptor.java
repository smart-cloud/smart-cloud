package org.smartframework.cloud.starter.mybatis.plugin;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.smartframework.cloud.mask.util.MaskUtil;
import org.smartframework.cloud.starter.log.util.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * mybatis sql日志打印
 *
 * @author liyulin
 * @date 2019-03-22
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "queryCursor", args = { MappedStatement.class, Object.class,
				RowBounds.class }) })
@Slf4j
public class MybatisSqlLogInterceptor implements Interceptor {

	/** sql最大长度限制 */
	private static final int SQL_LEN_LIMIT = 1 << 8;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object returnValue = null;
		try {
			returnValue = invocation.proceed();
			return returnValue;
		} finally {
			long time = System.currentTimeMillis() - start;
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			BoundSql boundSql = null;
			if (invocation.getArgs().length == 6) {
				boundSql = (BoundSql) invocation.getArgs()[5];
			} else {
				Object parameter = invocation.getArgs()[1];
				boundSql = mappedStatement.getBoundSql(parameter);
			}
			printSql(mappedStatement.getConfiguration(), boundSql, mappedStatement.getId(), time, returnValue);
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

	private void printSql(Configuration configuration, BoundSql boundSql, String sqlId, long time, Object returnValue) {
		String sqllog = concatSql(sqlId, boundSql.getSql(), MaskUtil.mask(boundSql.getParameterObject()), time,
				MaskUtil.mask(returnValue));

		log.info(LogUtil.truncate(sqllog));
	}

	/**
	 * sql日志拼接
	 *
	 * <p>
	 * 不能用换行。如果使用换行，在ELK中日志的顺序将会混乱
	 *
	 * @param sqlId
	 * @param sql
	 * @param inParams    入参
	 * @param time        sql执行时间
	 * @param returnValue sql执行结果
	 */
	private String concatSql(String sqlId, String sql, String inParams, long time, Object returnValue) {
		String separator = " ==> ";
		StringBuilder str = new StringBuilder((sql.length() > SQL_LEN_LIMIT) ? SQL_LEN_LIMIT : 64);
		str.append(getShortSqlId(sqlId));
		str.append("：");
		str.append(sql);
		str.append(separator);
		if (StringUtils.isNotBlank(inParams)) {
			str.append(inParams);
			str.append(separator);
		}
		str.append("spend：");
		str.append(time);
		str.append("ms");
		str.append(separator);
		str.append("result===>");
		str.append(returnValue);

		return str.toString();
	}

	/**
	 * 截取sqlId（只保留class.method）
	 *
	 * @param sqlId
	 * @return
	 */
	private String getShortSqlId(String sqlId) {
		for (int i = sqlId.length() - 1, times = 0; i >= 0; i--) {
			if (sqlId.charAt(i) == '.' && (++times) == 2) {
				return sqlId.substring(i + 1);
			}
		}

		return sqlId;
	}

}