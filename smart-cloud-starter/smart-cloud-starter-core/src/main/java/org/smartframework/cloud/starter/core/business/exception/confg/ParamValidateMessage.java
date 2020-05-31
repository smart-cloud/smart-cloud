package org.smartframework.cloud.starter.core.business.exception.confg;

import lombok.experimental.UtilityClass;

/**
 * 参数校验提示
 *
 * @author liyulin
 * @date 2019-06-29
 */
@UtilityClass
public class ParamValidateMessage {

	/** 请求参数中token缺失 */
	public static final String TOKEN_MISSING = "core.token.missing";
	/** 当前用户暂未登陆，获取userId失败 */
	public static final String GET_USERID_FAIL = "core.get.userid.fail";
	/** 未获取到登陆缓存信息 */
	public static final String LOGIN_CACHE_MISSING = "core.login.cache.missing";

}