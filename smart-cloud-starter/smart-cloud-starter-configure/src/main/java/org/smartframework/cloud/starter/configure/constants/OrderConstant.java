package org.smartframework.cloud.starter.configure.constants;

/**
 * bean执行顺序
 *
 * @author liyulin
 * @date 2019-06-28
 */
public interface OrderConstant {
	
	/** http filter */
	int HTTP_FITLER = Integer.MIN_VALUE;
	/** 重复提交校验 */
	int REPEAT_SUBMIT_CHECK = 1;
	/** 接口安全处理 */
	int API_SECURITY = 2;
	/** 接口日志 */
	int API_LOG = 3;
	/** feign安全处理 */
	int FEIGN_SECURITY = 4;
	/** feign日志 */
	int FEIGN_LOG = 5;
	/** 多语言切面 */
	int LOCALE = 6;

}