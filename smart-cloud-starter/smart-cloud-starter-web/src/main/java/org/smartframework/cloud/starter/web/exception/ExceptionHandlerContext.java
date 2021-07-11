package org.smartframework.cloud.starter.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.ResponseHead;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionHandlerContext {

	/**
	 * 将{@link Throwable}解析构造{@link ResponseHead}
	 * 
	 * @param e
	 * @return
	 */
	public static ResponseHead transRespHead(Throwable e) {
		for (IExceptionHandlerStrategy exceptionHandler : ExceptionHandlerStrategyFactory
				.getExceptionHandlerStrategys()) {
			if (!exceptionHandler.match(e)) {
				continue;
			}

			ResponseHead respHeadVO = exceptionHandler.transRespHead(e);
			if (respHeadVO != null) {
				return respHeadVO;
			}
		}

		if (e != null) {
			String message = e.getMessage();
			if (StringUtils.isBlank(message)) {
				message = e.toString();
				// 只取异常类名
				int index = message.lastIndexOf(SymbolConstant.DOT);
				if (index != -1) {
					message = message.substring(index + 1);
				}
			}

			return RespHeadUtil.of(CommonReturnCodes.SERVER_ERROR, message);
		}

		return RespHeadUtil.of(CommonReturnCodes.SERVER_ERROR, null);
	}

}