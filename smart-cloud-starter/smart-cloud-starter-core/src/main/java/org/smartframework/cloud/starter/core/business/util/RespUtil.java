package org.smartframework.cloud.starter.core.business.util;

import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;
import org.smartframework.cloud.common.pojo.vo.RespHeadVO;
import org.smartframework.cloud.common.pojo.vo.RespVO;
import org.smartframework.cloud.utility.ObjectUtil;

import lombok.experimental.UtilityClass;

/**
 * {@link RespVO}工具类
 *
 * @author liyulin
 * @date 2019-04-06
 */
@UtilityClass
public class RespUtil {

	/**
	 * 构造响应成功对象
	 * 
	 * @return
	 */
	public static <R extends Base> RespVO<R> success() {
		return new RespVO<>(new RespHeadVO(ReturnCodeEnum.SUCCESS));
	}

	/**
	 * 构造响应成功对象
	 * 
	 * @param r
	 * @return
	 */
	public static <R extends Base> RespVO<R> success(R r) {
		return new RespVO<>(r);
	}

	/**
	 * 构造响应错误对象
	 * 
	 * @param returnCode
	 * @return
	 */
	public static <R extends Base> RespVO<R> error(IBaseReturnCode returnCode) {
		return new RespVO<>(new RespHeadVO(returnCode));
	}
	
	/**
	 * 根据失败的响应对象，返回对应信息
	 * 
	 * @param resp
	 * @return
	 */
	public static <R extends Base, T extends Base> RespVO<T> error(RespVO<R> resp) {
		return error(getFailMsg(resp));
	}

	/**
	 * 构造响应错误对象
	 * 
	 * @param msg
	 * @return
	 */
	public static <R extends Base> RespVO<R> error(String msg) {
		return new RespVO<>(new RespHeadVO(ReturnCodeEnum.SERVER_ERROR.getCode(), msg));
	}

	/**
	 * 是否成功
	 * 
	 * @param resp
	 * @return
	 */
	public static <R extends Base> boolean isSuccess(RespVO<R> resp) {
		return ObjectUtil.isNotNull(resp) && ObjectUtil.isNotNull(resp.getHead())
				&& ObjectUtil.equals(ReturnCodeEnum.SUCCESS.getCode(), resp.getHead().getCode());
	}

	/**
	 * 获取失败的提示信息
	 * 
	 * @param resp
	 * @return
	 */
	public static <R extends Base> String getFailMsg(RespVO<R> resp) {
		if (ObjectUtil.isNull(resp)) {
			return "rpc请求失败";
		}

		if (ObjectUtil.isNull(resp.getHead())) {
			return "返回结果异常";
		}

		return resp.getHead().getMessage();
	}

}