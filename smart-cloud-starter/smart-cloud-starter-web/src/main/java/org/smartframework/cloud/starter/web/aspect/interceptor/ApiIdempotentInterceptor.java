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
import org.springframework.core.Ordered;

import com.alibaba.fastjson.JSON;

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
	/** md5的长度 */
	private static final int MD5_LEN = 32;

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

		String repeatSubmitCheckRedisKey = getRepeatSubmitCheckRedisKey(token);
		boolean result = false;
		try {
			Object reqObject = WebUtil.getRequestArgs(invocation.getArguments());
			if (reqObject != null) {
				String reqStrMd5 = getRepeatReqCheckKey(JSON.toJSONString(reqObject));
				Boolean success = redisComponent.setNx(repeatSubmitCheckRedisKey, reqStrMd5, idempotent.expireMillis());
				result = (success != null && success);
				if (result) {
					return invocation.proceed();
				} else {
					throw new RepeatSubmitException(idempotent.message());
				}
			}
		} finally {
			if (result) {
				redisComponent.delete(repeatSubmitCheckRedisKey);
			}
		}

		return invocation.proceed();
	}
	
	private final String getRepeatReqCheckKey(String reqStr) {
		if (reqStr.length() <= MD5_LEN) {
			return reqStr;
		}
		return DigestUtils.md5Hex(reqStr);
	}

	/**
	 * 重复提交校验redis key
	 * 
	 * @param token
	 * @return
	 */
	private final String getRepeatSubmitCheckRedisKey(String token) {
		return RedisKeyPrefix.LOCK.getKey() + RedisKeyPrefix.REDIS_KEY_SEPARATOR.getKey() + "rsc"
				+ RedisKeyPrefix.REDIS_KEY_SEPARATOR + token;
	}

}