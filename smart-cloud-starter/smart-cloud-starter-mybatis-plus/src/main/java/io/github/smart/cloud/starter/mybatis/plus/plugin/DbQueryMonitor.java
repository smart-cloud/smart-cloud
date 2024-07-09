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

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Collection;
import java.util.List;

/**
 * 数据库查询监控
 *
 * @author collin
 * @date 2024-06-18
 */
@Slf4j
@RequiredArgsConstructor
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class,
                RowBounds.class})})
public class DbQueryMonitor implements Interceptor {

    private final SmartProperties smartProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        try {
            printHugeQuery(returnValue, invocation);
        } catch (Exception e) {
            log.error("monitor mysql query fail", e);
        }
        return returnValue;
    }

    /**
     * 监控大数据量查询
     *
     * @param returnValue
     * @param invocation
     */
    private void printHugeQuery(Object returnValue, Invocation invocation) {
        if (returnValue == null) {
            return;
        }

        if (returnValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) returnValue;
            printHugeQuery(collection, invocation);
        } else if (returnValue instanceof IPage) {
            IPage<?> page = (IPage<?>) returnValue;
            List<?> records = page.getRecords();
            if (records != null) {
                printHugeQuery(records, invocation);
            }
        }
    }

    /**
     * 打印大数据量查询信息
     *
     * @param collection
     * @param invocation
     */
    private void printHugeQuery(Collection<?> collection, Invocation invocation) {
        int total = collection.size();
        if (total >= smartProperties.getDbQueryMonitor().getQueryMinSize()) {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            String sqlId = mappedStatement.getId();
            log.warn("hugedata.{}-->{}", sqlId, total);
        }
    }

    @Override
    public Object plugin(Object target) {
        // 只对要拦截的对象生成代理
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }

        return target;
    }

}