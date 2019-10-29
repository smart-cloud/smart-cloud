package org.smartframework.cloud.starter.common.business.util.exception;

import org.smartframework.cloud.common.pojo.dto.RespHead;

/**
 * @desc 接口异常转换
 * @author liyulin
 * @date 2019/10/29
 */
public interface IExceptionHandlerStrategy {

	/**
	 * 异常类型匹配
	 * 
	 * @param e
	 * @return
	 */
	boolean match(Throwable e);

	/**
	 * 将异常转化为响应体
	 * 
	 * @param e
	 * @return
	 */
	RespHead transRespHead(Throwable e);

}