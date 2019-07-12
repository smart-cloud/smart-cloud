package org.smartframework.cloud.utility.test.unit;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.security.RsaUtil;

import junit.framework.TestCase;

public class RsaUtilUnitTest extends TestCase {

	/**
	 * 加密、解密
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws DecoderException
	 * @throws InvalidKeySpecException
	 * 
	 * @throws Exception
	 */
	public void testEncryptAndDecrypt() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, DecoderException {
		KeyPair keyPair = RsaUtil.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		String plainText = "hello world!";
		// 加密后的文本
		String encryptText = RsaUtil.encryptString(publicKey, plainText);

		String modulus = RsaUtil.getModulus(keyPair);
		String privateExponent = RsaUtil.getPrivateExponent(keyPair);
		RSAPrivateKey decryptPrivateKey = RsaUtil.getRSAPrivateKey(modulus, privateExponent);

		// 解密后的文本
		String decryptText = RsaUtil.decryptString(decryptPrivateKey, encryptText);

		Assertions.assertThat(plainText).isEqualTo(decryptText);
	}

	/**
	 * 签名
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws DecoderException
	 */
	public void testSign() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException,
			SignatureException, UnsupportedEncodingException, DecoderException {
		KeyPair keyPair = RsaUtil.generateKeyPair();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

		String content = "hello world!";
		String sign = RsaUtil.sign(content, rsaPrivateKey);
		boolean result = RsaUtil.checkSign(content, sign, rsaPublicKey);
		Assertions.assertThat(result).isTrue();
	}

	public void testGenerateRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
		KeyPair keyPair = RsaUtil.generateKeyPair();
		String publicExponent = RsaUtil.getPublicExponent(keyPair);
		String modulus = RsaUtil.getModulus(keyPair);
		RsaUtil.getRSAPublidKey(modulus, publicExponent);
	}

	public void testDecryptStringByJs() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, DecoderException {
		KeyPair keyPair = RsaUtil.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		String plainText = "hello world!";
		// 加密后的文本
		String encryptText = RsaUtil.encryptString(publicKey, plainText);
		// 解密后的文本
		String decryptPrivateKey = RsaUtil.decryptStringByJs(keyPair.getPrivate(), encryptText);

		Assertions.assertThat(StringUtils.reverse(decryptPrivateKey)).isEqualTo(plainText);
	}

}