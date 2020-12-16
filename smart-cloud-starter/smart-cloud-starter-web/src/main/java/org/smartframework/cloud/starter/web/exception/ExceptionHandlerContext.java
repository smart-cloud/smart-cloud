package org.smartframework.cloud.starter.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.starter.core.business.util.RespHeadUtil;
import org.smartframework.cloud.starter.core.constants.SymbolConstant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionHandlerContext {

	/**
	 * 将{@link Throwable}解析构造{@link RespHeadVO}
	 * 
	 * @param e
	 * @return
	 */
	public static RespHeadVO transRespHead(Throwable e) {
		for (AbstractExceptionHandlerStrategy exceptionHandler : ExceptionHandlerStrategyFactory
				.getExceptionHandlerStrategys()) {
			if (!exceptionHandler.match(e)) {
				continue;
			}

			RespHeadVO respHeadVO = exceptionHandler.transRespHead(e);
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

			return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, message);
		}

		return RespHeadUtil.of(ReturnCodeEnum.SERVER_ERROR, null);
	}

}