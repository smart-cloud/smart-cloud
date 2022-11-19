package io.github.smart.cloud.starter.core.business;

import io.github.smart.cloud.starter.configure.properties.AsyncProperties;
import io.github.smart.cloud.utility.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
public class SmartAsyncConfigurerSupport extends AsyncConfigurerSupport {

    private final AsyncProperties asyncProperties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setCorePoolSize(asyncProperties.getCorePoolSize());
        threadPoolTaskExecutor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setQueueCapacity(asyncProperties.getQueueCapacity());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(asyncProperties.getAwaitTerminationSeconds());
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    @Override
    @Nullable
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> log.error("asyncException@method={}; param={}", method.getName(), JacksonUtil.toJson(obj), throwable);
    }

}