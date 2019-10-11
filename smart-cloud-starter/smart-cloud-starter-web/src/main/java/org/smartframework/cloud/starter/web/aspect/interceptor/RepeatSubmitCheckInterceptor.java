package org.smartframework.cloud.starter.web.aspect.interceptor;

import java.lang.reflect.Method;
import java.util.Objects;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.common.business.exception.RepeatSubmitException;
import org.smartframework.cloud.starter.common.business.security.util.ReqHttpHeadersUtil;
import org.smartframework.cloud.starter.common.business.util.WebUtil;
import org.smartframework.cloud.starter.common.constants.RedisKeyPrefix;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.smartframework.cloud.starter.redis.component.RedisComponent;
import org.smartframework.cloud.starter.web.annotation.RepeatReqValidate;
import org.springframework.core.Ordered;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;

/**
 * 重复提交校验拦截器
 * 
 * @author liyulin
 * @date 2019-06-13
 */
@AllArgsConstructor
public class RepeatSubmitCheckInterceptor implements MethodInterceptor, Ordered {

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
		RepeatReqValidate validate = method.getAnnotation(RepeatReqValidate.class);
		if (Objects.isNull(validate)) {
			return invocation.proceed();
		}

		String repeatSubmitCheckRedisKey = getRepeatSubmitCheckRedisKey(token);
		boolean result = false;
		try {
			Object reqObject = WebUtil.getRequestArgs(invocation.getArguments());
			if (reqObject != null) {
				String reqStrMd5 = DigestUtils.md5Hex(JSON.toJSONString(reqObject));
				Boolean success = redisComponent.setNx(repeatSubmitCheckRedisKey, reqStrMd5, validate.expireMillis());
				result = (success != null && success);
				if (result) {
					return invocation.proceed();
				} else {
					throw new RepeatSubmitException(validate.message());
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
	 * @return
	 */
	private String getRepeatSubmitCheckRedisKey(String token) {
		return RedisKeyPrefix.API + RedisKeyPrefix.REDIS_KEY_SEPARATOR + "rsc" + RedisKeyPrefix.REDIS_KEY_SEPARATOR
				+ token;
	}

}