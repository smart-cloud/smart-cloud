package org.smartframework.cloud.starter.web.aspect.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.common.business.exception.ParamValidateException;
import org.smartframework.cloud.starter.configure.constants.OrderConstant;
import org.springframework.core.Ordered;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMethod;

public class ApiSecurityInterceptor implements MethodInterceptor, Ordered {

	/** 请求的最大有效时间间隔（2分钟） */
	private final long REQUEST_MAX_EXPIRE_MILLIS = 1000 * 60 * 2L;

	@Override
	public int getOrder() {
		return OrderConstant.API_SECURITY;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		/*HttpServletRequest request = WebUtil.getHttpServletRequest();
		String httpMethod = request.getMethod();

		validateRequestMethod(httpMethod);

		ReqHttpHeadersDto reqHttpHeaders = ReqHttpHeadersUtil.getReqHttpHeadersDto();
		// 1、校验请求timestamp
		validateTimestamp(reqHttpHeaders.getTimestamp());

		// 2、校验token
		String token = reqHttpHeaders.getToken();
		if (StringUtils.isBlank(token)) {
			return invocation.proceed();
		}

		String requestURI = request.getRequestURI();
		String contentType = request.getContentType();
		// 非json（如上传文件）过滤
		if (StringUtils.isBlank(contentType) || !contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
			return invocation.proceed();
		}
		// 上传文件，怎么判断？MultipartHttpServletRequest?
		
		if (requestURI.contains(ApiTypeEnum.OPEN.getPathSegment())) {
			return invocation.proceed();
		}

		if (requestURI.contains(ApiTypeEnum.SIGN.getPathSegment())) {
			String queryString = request.getQueryString();
			// 签名
			if (RequestMethod.GET.toString().equals(httpMethod)) {
//				String queryString = request.getQueryString();
			} else {
				
			}
			// 加密
			return false;
		}
		if (requestURI.contains(ApiTypeEnum.IDENTITY.getPathSegment())
				|| requestURI.contains(ApiTypeEnum.AUTH.getPathSegment())) {
			if (!ReqContextHolder.isLogin()) {
				throw new NotLoggedInException();
			}
			if (requestURI.contains(ApiTypeEnum.AUTH.getPathSegment())) {
				// 签名
				// 加密
			}
			// 鉴权
		}*/

		return invocation.proceed();
	}

	/**
	 * 校验http请求方式（只支持get、post）
	 * 
	 * @param httpMethod
	 * @throws HttpRequestMethodNotSupportedException
	 */
	private void validateRequestMethod(String httpMethod) throws HttpRequestMethodNotSupportedException {
		if (!RequestMethod.GET.toString().equals(httpMethod) && !RequestMethod.POST.toString().equals(httpMethod)) {
			throw new HttpRequestMethodNotSupportedException(httpMethod);
		}
	}

	/**
	 * 校验请求timestamp
	 * 
	 * @param timestamp
	 */
	private void validateTimestamp(String timestamp) {
		if (StringUtils.isBlank(timestamp)) {
			throw new ParamValidateException("请求timestamp不能为空");
		}
		if (!StringUtils.isNumeric(timestamp)) {
			throw new ParamValidateException("请求timestamp格式不正确");
		}
		long currentTimeMillis = System.currentTimeMillis();
		long requestTimeMillis = Long.parseLong(timestamp);
		long requestTimeRange = currentTimeMillis - requestTimeMillis;
		if (requestTimeRange < 0 || requestTimeRange > REQUEST_MAX_EXPIRE_MILLIS) {
			throw new ParamValidateException("请求timestamp非法");
		}
	}

}