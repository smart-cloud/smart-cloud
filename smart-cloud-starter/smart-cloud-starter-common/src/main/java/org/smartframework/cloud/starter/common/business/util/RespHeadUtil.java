package org.smartframework.cloud.starter.common.business.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.utility.NonceUtil;

import lombok.experimental.UtilityClass;

/**
 * {@link RespHeadVO}工具类
 *
 * @author liyulin
 * @date 2019-04-14
 */
@UtilityClass
public class RespHeadUtil {

	/**
	 * 构造RespHead对象
	 * 
	 * @return 默认返回状态码{@code ReturnCodeEnum.SUCCESS}
	 */
	public static RespHeadVO of() {
		return of(ReturnCodeEnum.SUCCESS, null);
	}

	/**
	 * 构造RespHead对象
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	public static RespHeadVO of(String code, String message) {
		RespHeadVO respHeadVO = new RespHeadVO(code, message);
		respHeadVO.setTimestamp(System.currentTimeMillis());
		respHeadVO.setNonce(NonceUtil.getInstance().nextId());

		return respHeadVO;
	}

	/**
	 * 构造RespHead对象
	 * 
	 * @param returnCode
	 * @param message
	 * @return
	 */
	public static RespHeadVO of(IBaseReturnCode returnCode, String message) {
		RespHeadVO respHeadVO = new RespHeadVO(returnCode);
		respHeadVO.setTimestamp(System.currentTimeMillis());
		respHeadVO.setNonce(NonceUtil.getInstance().nextId());
		if (StringUtils.isNotBlank(message)) {
			respHeadVO.setMessage(message);
		}

		return respHeadVO;
	}

}