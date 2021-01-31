package org.smartframework.cloud.starter.core.business.util;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.utility.NonceUtil;

import lombok.experimental.UtilityClass;
import org.smartframework.cloud.utility.spring.I18NUtil;

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
		return of(CommonReturnCodes.SUCCESS, null);
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
	 * @param returnCodes
	 * @param message
	 * @return
	 */
	public static RespHeadVO of(IBaseReturnCodes returnCodes, String message) {
		RespHeadVO respHeadVO = new RespHeadVO(returnCodes);
		respHeadVO.setTimestamp(System.currentTimeMillis());
		respHeadVO.setNonce(NonceUtil.getInstance().nextId());
		if (StringUtils.isNotBlank(message)) {
			respHeadVO.setMessage(message);
		}

		return respHeadVO;
	}

	/**
	 * 构造RespHead对象（i18n message）
	 *
	 * @param returnCodes
	 * @return
	 */
	public static RespHeadVO ofI18n(IBaseReturnCodes returnCodes) {
		RespHeadVO respHeadVO = new RespHeadVO(returnCodes);
		respHeadVO.setTimestamp(System.currentTimeMillis());
		respHeadVO.setNonce(NonceUtil.getInstance().nextId());
		respHeadVO.setMessage(I18NUtil.getMessage(returnCodes.getCode()));

		return respHeadVO;
	}

}