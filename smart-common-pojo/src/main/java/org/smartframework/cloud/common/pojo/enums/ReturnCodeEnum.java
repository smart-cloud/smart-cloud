package org.smartframework.cloud.common.pojo.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态码
 * 
 * @author liyulin
 * @date 2019-03-27
 * @see IBaseReturnCode
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ReturnCodeEnum implements IBaseReturnCode {

	/** 校验失败 */
	VALIDATE_FAIL("100101", "common.validate.fail"),
	/** 数据不存在 */
	DATA_MISSING("100102", "common.data.missing"),
	/** 数据已存在 */
	DATA_EXISTED("100103", "common.data.existed"),
	/** 成功 */
	SUCCESS("100200", "common.success"),
	/** 签名错误 */
	SIGN_ERROR("100400", "common.sign.error"),
	/** 无权限访问 */
	NO_ACCESS("100401", "common.no.access"),
	/** 请求url错误 */
	REQUEST_URL_ERROR("100404", "common.request.url.error"),
	/** 请求超时 */
	REQUEST_TIMEOUT("100408", "common.request.timeout"),
	/** 重复提交 */
	REPEAT_SUBMIT("100409", "common.repeat.submit"),
	/** 参数不全 */
	PARAMETERS_MISSING("100412", "common.parameters.missing"),
	/** 请求方式不支持 */
	REQUEST_METHOD_NOT_SUPPORTED("100415", "common.request.method.not.supported"),
	/** 请求类型不支持 */
	UNSUPPORTED_MEDIA_TYPE("100416", "common.unsupported.media.type"),
	/** 获取锁失败 */
	GET_LOCK_FAIL("100417", "common.get.lock.fail"),
	/** 上传文件大小超过限制 */
	UPLOAD_FILE_SIZE_EXCEEDED("100418", "common.upload.file.size.exceeded"),
	/** 当前会话已失效，请重新登陆 */
	NOT_LOGGED_IN("100419", "common.not.logged.in"),
	/** 服务器异常 */
	SERVER_ERROR("100500", "common.server.error"),
	/** 获取Request失败 */
	GET_REQUEST_FAIL("100501", "common.get.request.fail"),
	/** 获取Response失败 */
	GET_RESPONSE_FAIL("100502", "get.response.fail");

	/** 状态码 */
	private String code;
	/** 提示信息 */
	private String message;

}