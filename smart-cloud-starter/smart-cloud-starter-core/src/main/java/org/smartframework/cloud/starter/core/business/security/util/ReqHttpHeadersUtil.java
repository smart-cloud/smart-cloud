package org.smartframework.cloud.starter.core.business.security.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.core.business.exception.ParamValidateException;
import org.smartframework.cloud.starter.core.business.exception.confg.ParamValidateMessage;
import org.smartframework.cloud.starter.core.business.security.ReactiveRequestContextHolder;
import org.smartframework.cloud.starter.core.business.security.bo.ReqHttpHeadersBO;
import org.smartframework.cloud.starter.core.business.security.enums.ReqHttpHeadersEnum;
import org.smartframework.cloud.starter.core.business.util.SnowFlakeIdUtil;
import org.smartframework.cloud.starter.core.business.util.WebServletUtil;
import org.smartframework.cloud.starter.core.business.util.WebUtil;
import org.smartframework.cloud.utility.RandomUtil;
import org.springframework.http.HttpHeaders;

import lombok.experimental.UtilityClass;

/**
 * {@link ReqHttpHeadersBO}工具类
 * 
 * @author liyulin
 * @date 2019-06-27
 */
@UtilityClass
public class ReqHttpHeadersUtil {

	/**
	 * 从<code>HttpServletRequest</code>中获取请求头信息
	 * 
	 * @return
	 */
	public static ReqHttpHeadersBO getReqHttpHeadersBO() {
		String token, nonce, timestamp, sign;
		if (WebUtil.isWebFlux()) {
			HttpHeaders httpHeaders = ReactiveRequestContextHolder.getHttpHeaders();
			token = httpHeaders.getFirst(ReqHttpHeadersEnum.SMART_TOKEN.getHeaderName());
			nonce = httpHeaders.getFirst(ReqHttpHeadersEnum.SMART_NONCE.getHeaderName());
			timestamp = httpHeaders.getFirst(ReqHttpHeadersEnum.SMART_TIMESTAMP.getHeaderName());
			sign = httpHeaders.getFirst(ReqHttpHeadersEnum.SMART_SIGN.getHeaderName());
		} else {
			HttpServletRequest request = WebServletUtil.getHttpServletRequest();
			token = request.getHeader(ReqHttpHeadersEnum.SMART_TOKEN.getHeaderName());
			nonce = request.getHeader(ReqHttpHeadersEnum.SMART_NONCE.getHeaderName());
			timestamp = request.getHeader(ReqHttpHeadersEnum.SMART_TIMESTAMP.getHeaderName());
			sign = request.getHeader(ReqHttpHeadersEnum.SMART_SIGN.getHeaderName());
		}

		return ReqHttpHeadersBO.builder().token(token).nonce(nonce).timestamp(timestamp).sign(sign).build();
	}

	/**
	 * 生成token
	 * 
	 * @return
	 */
	public static String generateToken() {
		// 产生规则：16进制（雪花算法）+2位随机字符混淆
		return Long.toHexString(SnowFlakeIdUtil.getInstance().nextId()) + RandomUtil.generateRandom(false, 2);
	}

	/**
	 * 获取请求参数中的token；如果不存在，则抛异常
	 * 
	 * @return
	 */
	public static String getTokenMustExist() {
		String token = getAvailableToken();
		if (StringUtils.isBlank(token)) {
			throw new ParamValidateException(ParamValidateMessage.TOKEN_MISSING);
		}
		return token;
	}

	/**
	 * 获取请求参数中的token（有可能不存在）
	 * 
	 * @return
	 */
	public static String getAvailableToken() {
		if (WebUtil.isWebFlux()) {
			HttpHeaders httpHeaders = ReactiveRequestContextHolder.getHttpHeaders();
			return httpHeaders.getFirst(ReqHttpHeadersEnum.SMART_TOKEN.getHeaderName());
		}
		
		HttpServletRequest request = WebServletUtil.getHttpServletRequest();
		return request.getHeader(ReqHttpHeadersEnum.SMART_TOKEN.getHeaderName());
	}

}