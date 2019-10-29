package org.smartframework.cloud.starter.common.business.util.exception;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.starter.common.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.common.constants.SymbolConstant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionHandlerContext {

	/**
	 * 将{@link Throwable}解析构造{@link RespHead}
	 * 
	 * @param e
	 * @return
	 */
	public static RespHead transRespHead(Throwable e) {
		for (IExceptionHandlerStrategy exceptionHandler : ExceptionHandlerStrategyFactory
				.getExceptionHandlerStrategys()) {
			if (!exceptionHandler.match(e)) {
				continue;
			}

			RespHead respHead = exceptionHandler.transRespHead(e);
			if (respHead != null) {
				return respHead;
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

			return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, message);
		}

		return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, null);
	}

}