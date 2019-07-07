package org.smartframework.cloud.starter.common.business.security.util;

import org.smartframework.cloud.utility.security.AesUtil;

import com.alibaba.fastjson.JSON;

public class SmartReqUtil {

	public static String encryptReq(Object body, String password) {
		return AesUtil.encrypt(JSON.toJSONString(body), password);
	}
	
}