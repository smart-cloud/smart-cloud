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
import io.github.smart.cloud.utility.JacksonUtil;
import io.github.smart.cloud.utility.concurrent.NamedThreadFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

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
public class DbQueryMonitor implements Interceptor, InitializingBean, DisposableBean {

    private final SmartProperties smartProperties;
    private final ConcurrentMap<String, LongAdder> queryStatistics = new ConcurrentHashMap<>();
    private final CreateMysqlQueryAdderFunction createMysqlQueryAdderFunction = new CreateMysqlQueryAdderFunction();
    private ScheduledExecutorService printAdderCallSchedule = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        try {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            String sqlId = mappedStatement.getId();

            adderCallTimes(sqlId);
            printHugeQuery(returnValue, sqlId);
        } catch (Exception e) {
            log.error("monitor mysql query fail", e);
        }
        return returnValue;
    }

    /**
     * 统计调用次数
     *
     * @param sqlId
     */
    private void adderCallTimes(String sqlId) {
        LongAdder adder = queryStatistics.computeIfAbsent(sqlId, createMysqlQueryAdderFunction);
        adder.add(1);
    }

    /**
     * 监控大数据量查询
     *
     * @param returnValue
     * @param sqlId
     */
    private void printHugeQuery(Object returnValue, String sqlId) {
        if (returnValue == null) {
            return;
        }

        if (returnValue instanceof Collection) {
            Collection<?> collection = (Collection<?>) returnValue;
            printHugeQuery(collection, sqlId);
        } else if (returnValue instanceof IPage) {
            IPage<?> page = (IPage<?>) returnValue;
            List<?> records = page.getRecords();
            if (records != null) {
                printHugeQuery(records, sqlId);
            }
        }
    }

    /**
     * 打印大数据量查询信息
     *
     * @param collection
     * @param sqlId
     */
    private void printHugeQuery(Collection<?> collection, String sqlId) {
        int total = collection.size();
        if (total >= smartProperties.getDbQueryMonitor().getQueryMinSize()) {
            log.warn("hugedata.{}-->{}", sqlId, total);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        printAdderCallSchedule = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("print-adder-call"));
        printAdderCallSchedule.scheduleWithFixedDelay(this::printAdderCallTimes, 3, 30, TimeUnit.MINUTES);
    }

    private void printAdderCallTimes() {
        List<QueryCallDTO> data = new ArrayList<>();
        for (Map.Entry<String, LongAdder> entry : queryStatistics.entrySet()) {
            data.add(new QueryCallDTO(entry.getKey(), entry.getValue().sum()));
        }

        if (smartProperties.getDbQueryMonitor().isCleanQueryStatistics()) {
            queryStatistics.clear();
        }

        Collections.sort(data, Comparator.comparing(QueryCallDTO::getCallCount).reversed());
        if (data.size() > smartProperties.getDbQueryMonitor().getPrintMaxSize()) {
            data = data.subList(0, smartProperties.getDbQueryMonitor().getPrintMaxSize());
        }

        log.warn("query call count statistics:{}", JacksonUtil.toJson(data));
    }

    @Override
    public void destroy() throws Exception {
        if (printAdderCallSchedule != null) {
            printAdderCallSchedule.shutdown();
        }
    }

    static class CreateMysqlQueryAdderFunction implements Function<String, LongAdder> {

        @Override
        public LongAdder apply(String s) {
            return new LongAdder();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class QueryCallDTO {
        private String sqlId;
        private long callCount;
    }

}