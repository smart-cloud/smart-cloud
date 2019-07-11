package org.smartframework.cloud.starter.common.business.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.dto.RespHead;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.utility.NonceUtil;

import lombok.experimental.UtilityClass;

/**
 * {@link RespHead}工具类
 *
 * @author liyulin
 * @date 2019年4月14日下午5:47:57
 */
@UtilityClass
public class RespHeadUtil {

	/**
	 * 构造RespHead对象
	 * 
	 * @return 默认返回状态码{@code ReturnCodeEnum.SUCCESS}
	 */
	public static RespHead of() {
		return of(ReturnCodeEnum.SUCCESS, null);
	}

	/**
	 * 构造RespHead对象
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static RespHead of(String code, String message) {
		RespHead respHead = new RespHead(code, message);
		respHead.setTimestamp(System.currentTimeMillis());
		respHead.setNonce(NonceUtil.getInstance().nextId());

		return respHead;
	}

	/**
	 * 构造RespHead对象
	 * 
	 * @param returnCode
	 * @param message
	 * @return
	 */
	public static RespHead of(IBaseReturnCode returnCode, String message) {
		RespHead respHead = new RespHead(returnCode);
		respHead.setTimestamp(System.currentTimeMillis());
		respHead.setNonce(NonceUtil.getInstance().nextId());
		if (StringUtils.isNotBlank(message)) {
			respHead.setMessage(message);
		}

		return respHead;
	}

}