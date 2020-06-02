package org.smartframework.cloud.starter.web.aspect.interceptor;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.core.business.exception.RepeatSubmitException;
import org.smartframework.cloud.starter.core.business.security.util.ReqHttpHeadersUtil;
import org.smartframework.cloud.starter.core.business.util.WebUtil;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.smartframework.cloud.starter.redis.enums.RedisKeyPrefix;
import org.smartframework.cloud.starter.web.annotation.ApiIdempotent;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.spring.I18NUtil;
import org.springframework.core.Ordered;

import lombok.AllArgsConstructor;

/**
 * 幂等校验拦截器
 * 
 * @author liyulin
 * @date 2019-06-13
 */
@AllArgsConstructor
public class ApiIdempotentInterceptor implements MethodInterceptor, Ordered {

	private RedisComponent redisComponent;

	@Override
	public int getOrder() {
		return OrderConstant.REPEAT_SUBMIT_CHECK;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String token = ReqHttpHeadersUtil.getAvailableToken();
		if (StringUtils.isBlank(token)) {
			return invocation.proceed();
		}

		Method method = invocation.getMethod();
		ApiIdempotent idempotent = method.getAnnotation(ApiIdempotent.class);
		if (Objects.isNull(idempotent)) {
			return invocation.proceed();
		}

		
		String repeatSubmitCheckRedisKey = null;
		boolean result = false;
		try {
			Object reqObject = WebUtil.getRequestArgs(invocation.getArguments());
			if (reqObject != null) {
				repeatSubmitCheckRedisKey = getRepeatSubmitCheckRedisKey(token, JacksonUtil.toJson(reqObject));
				Boolean success = redisComponent.setNx(repeatSubmitCheckRedisKey, "", idempotent.expireMillis());
				result = (success != null && success);
				if (result) {
					return invocation.proceed();
				} else {
					throw new RepeatSubmitException(I18NUtil.getMessage(idempotent.message()));
				}
			}
		} finally {
			if (result) {
				redisComponent.delete(repeatSubmitCheckRedisKey);
			}
		}

		return invocation.proceed();
	}
	
	/**
	 * 重复提交校验redis key
	 * 
	 * @param token
	 * @param requestParamJson
	 * @return
	 */
	private final String getRepeatSubmitCheckRedisKey(String token, String requestParamJson) {
		// url:token:method:request_params
		String key = WebUtil.getMappingPath()+token+WebUtil.getHttpMethod()+ requestParamJson;
		return RedisKeyPrefix.LOCK.getKey() + "rsc" + RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey() + DigestUtils.md5Hex(key);
	}

}