package org.smartframework.cloud.starter.common.constants;

import lombok.experimental.UtilityClass;

/**
 * bean执行顺利
 *
 * @author liyulin
 * @date 2019-06-28
 */
@UtilityClass
public class OrderConstant {

	/** 重复提交校验 */
	public static final int REPEAT_SUBMIT_CHECK = 1;
	/** 接口安全处理 */
	public static final int API_SECURITY = 2;
	/** 接口日志 */
	public static final int API_LOG = 3;
	/** feign安全处理 */
	public static final int FEIGN_SECURITY = 4;
	/** feign日志 */
	public static final int FEIGN_LOG = 5;

}