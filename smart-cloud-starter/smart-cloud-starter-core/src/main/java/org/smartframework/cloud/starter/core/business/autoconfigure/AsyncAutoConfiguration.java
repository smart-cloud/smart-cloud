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
package org.smartframework.cloud.starter.core.business.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.utility.JacksonUtil;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步配置
 *
 * @author collin
 * @date 2021-10-31
 */
@Configuration
@ConditionalOnProperty(prefix = "smart", name = "async", havingValue = "true", matchIfMissing = true)
@Slf4j
public class AsyncAutoConfiguration extends AsyncConfigurerSupport {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    /**
     * 池大小 = ((核心数 * 2) + 有效磁盘数)
     */
    private static final int MAX_POOL_SIZE = (CORE_POOL_SIZE << 1) + 1;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);

        return threadPoolTaskExecutor;
    }

    @Override
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> log.error("asyncException@method=" + method.getName() + "; param=" + JacksonUtil.toJson(obj), throwable);
    }

}