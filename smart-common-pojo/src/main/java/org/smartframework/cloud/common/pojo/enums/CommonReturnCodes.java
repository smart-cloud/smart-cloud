package org.smartframework.cloud.common.pojo.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态码
 * 
 * @author liyulin
 * @date 2019-03-27
 * @see IBaseReturnCodes
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonReturnCodes implements IBaseReturnCodes {

	/** 成功 */
	SUCCESS("200"),
	/** 校验失败 */
	VALIDATE_FAIL("101"),
	/** 数据不存在 */
	DATA_MISSING("102"),
	/** 数据已存在 */
	DATA_EXISTED("103"),
	/** 签名错误 */
	SIGN_ERROR("400"),
	/** 无权限访问 */
	NO_ACCESS("401"),
	/** 请求url错误 */
	REQUEST_URL_ERROR("404"),
	/** 请求超时 */
	REQUEST_TIMEOUT("408"),
	/** 重复提交 */
	REPEAT_SUBMIT("409"),
	/** 参数不全 */
	PARAMETERS_MISSING("412"),
	/** 请求方式不支持 */
	REQUEST_METHOD_NOT_SUPPORTED("415"),
	/** 请求类型不支持 */
	UNSUPPORTED_MEDIA_TYPE("416"),
	/** 获取锁失败 */
	GET_LOCK_FAIL("417"),
	/** 上传文件大小超过限制 */
	UPLOAD_FILE_SIZE_EXCEEDED("418"),
	/** 当前会话已失效，请重新登陆 */
	NOT_LOGGED_IN("419"),
	/** 服务器异常 */
	SERVER_ERROR("500"),
	/** 获取Request失败 */
	GET_REQUEST_FAIL("501"),
	/** 获取Response失败 */
	GET_RESPONSE_FAIL("502");

	/** 状态码 */
	private String code;

}