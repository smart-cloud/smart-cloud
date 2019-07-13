package org.smartframework.cloud.starter.common.business.security.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.DecoderException;
import org.smartframework.cloud.starter.common.business.security.dto.ReqHttpHeadersDto;
import org.smartframework.cloud.starter.common.business.security.dto.RespDto;
import org.smartframework.cloud.starter.common.business.security.enums.ReqHttpHeadersEnum;
import org.smartframework.cloud.utility.security.RsaUtil;

import com.alibaba.fastjson.JSON;

import lombok.experimental.UtilityClass;

/**
 * 请求响应信息签名工具类
 * 
 * @author liyulin
 * @date 2019-06-27
 */
@UtilityClass
public class SmartSignatureUtil {
	private static final String SIGN_HEAD_NAME = "head";
	private static final String SIGN_BODY_NAME = "body";

	/**
	 * 请求参数签名
	 * 
	 * @param reqHttpHeaders 请求头参数
	 * @param encryptedBody  加密后的请求体
	 * @param rsaPrivateKey  签名私钥
	 * @return
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static String signReq(ReqHttpHeadersDto reqHttpHeaders, String encryptedBody, RSAPrivateKey rsaPrivateKey)
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException,
			UnsupportedEncodingException {
		String signContent = getReqSignContent(reqHttpHeaders, encryptedBody);
		return RsaUtil.sign(signContent, rsaPrivateKey);
	}

	/**
	 * 校验请求参数签名
	 * 
	 * @param reqHttpHeaders
	 * @param encryptedBody
	 * @param rsaPublicKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 * @throws DecoderException
	 */
	public static boolean checkReqSign(ReqHttpHeadersDto reqHttpHeaders, String encryptedBody,
			RSAPublicKey rsaPublicKey) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException,
			SignatureException, UnsupportedEncodingException, DecoderException {
		String signContent = getReqSignContent(reqHttpHeaders, encryptedBody);
		return RsaUtil.checkSign(signContent, reqHttpHeaders.getSign(), rsaPublicKey);
	}

	/**
	 * 获取请求信息待签名的内容
	 * 
	 * @param reqHttpHeaders
	 * @param encryptedBody
	 * @return
	 */
	private static String getReqSignContent(ReqHttpHeadersDto reqHttpHeaders, String encryptedBody) {
		SortedMap<String, String> signParams = new TreeMap<>();
		signParams.put(ReqHttpHeadersEnum.SMART_TIMESTAMP.getHeaderName(), reqHttpHeaders.getTimestamp());
		signParams.put(ReqHttpHeadersEnum.SMART_TOKEN.getHeaderName(), reqHttpHeaders.getToken());
		signParams.put(ReqHttpHeadersEnum.SMART_NONCE.getHeaderName(), reqHttpHeaders.getNonce());
		signParams.put(SIGN_BODY_NAME, encryptedBody);
		return JSON.toJSONString(signParams);
	}

	/**
	 * 响应信息签名
	 * 
	 * @param encryptedHead
	 * @param encryptedBody
	 * @param rsaPrivateKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static String signResp(String encryptedHead, String encryptedBody, RSAPrivateKey rsaPrivateKey)
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException,
			UnsupportedEncodingException {
		String signContent = getRespSignContent(encryptedHead, encryptedBody);
		return RsaUtil.sign(signContent, rsaPrivateKey);
	}

	/**
	 * 校验响应参数签名
	 * 
	 * @param respDto
	 * @param rsaPublicKey
	 * @return
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 * @throws DecoderException
	 */
	public static boolean checkRespSign(RespDto respDto,
			RSAPublicKey rsaPublicKey) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException,
			SignatureException, UnsupportedEncodingException, DecoderException {
		String signContent = getRespSignContent(respDto.getHead(), respDto.getBody());
		return RsaUtil.checkSign(signContent, respDto.getSign(), rsaPublicKey);
	}
	
	/**
	 * 获取响应信息待签名的内容
	 * 
	 * @param encryptedHead
	 * @param encryptedBody
	 * @return
	 */
	private static String getRespSignContent(String encryptedHead, String encryptedBody) {
		SortedMap<String, String> signParams = new TreeMap<>();
		signParams.put(SIGN_HEAD_NAME, encryptedHead);
		signParams.put(SIGN_BODY_NAME, encryptedBody);

		return JSON.toJSONString(signParams);
	}

}