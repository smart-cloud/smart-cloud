package org.smartframework.cloud.starter.core.business.util;

import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.vo.BasePageReqVO;

import lombok.experimental.UtilityClass;

/**
 * 请求对象工具类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@UtilityClass
public class ReqUtil {



	/**
	 * 构建{@code BasePageReq<T>}对象
	 * 
	 * @param body
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public static <T extends Base> BasePageReqVO<T> build(T body, Integer pageNum, Integer pageSize) {
		return new BasePageReqVO<>(body, pageNum, pageSize);
	}

}