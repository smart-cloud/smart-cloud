package org.smartframework.cloud.starter.common.business.autoconfigure;

import java.util.concurrent.Executor;

import org.smartframework.cloud.starter.log.util.LogUtil;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;

@Configuration
@ConditionalOnProperty(prefix = "smart", name = "async", havingValue = "true")
public class AsyncAutoConfigure extends AsyncConfigurerSupport {

	private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	/** 池大小 = ((核心数 * 2) + 有效磁盘数) */
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
		return (throwable, method, obj) -> LogUtil.error("asyncException@method=" + method.getName() + "; param=" + JSON.toJSONString(obj), throwable);
	}

}