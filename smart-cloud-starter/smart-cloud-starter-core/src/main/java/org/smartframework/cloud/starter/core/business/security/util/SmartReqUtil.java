package org.smartframework.cloud.starter.core.business.security.util;

import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.security.AesUtil;

public class SmartReqUtil {

	public static String encryptReq(Object body, String password) {
		return AesUtil.encrypt(JacksonUtil.toJson(body), password);
	}
	
}