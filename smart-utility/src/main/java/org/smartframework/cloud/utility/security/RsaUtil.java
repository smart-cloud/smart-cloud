package org.smartframework.cloud.utility.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.experimental.UtilityClass;

/**
 * RSA非对称加密工具类
 * 
 * @author liyulin
 * @date 2019年6月24日 下午8:59:59
 */
@UtilityClass
public class RsaUtil {

	/** 加密算法名称 */
	private static final String ALGORITHOM = "RSA";
	/** 签名算法名称 */
	private static final String SIGNATURE_ALGORITHOM = "SHA256WithRSA";
	/** 密钥大小 */
	private static final int DEFAULT_KEY_SIZE = 512;
	private static final String CHARSET_NAME = StandardCharsets.UTF_8.name();
	/** 默认的安全服务提供者 */
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	private static KeyPairGenerator getKeyPairGenerator() throws NoSuchAlgorithmException {
		return KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
	}

	private static KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
		return KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
	}

	/**
	 * 生成并返回RSA密钥对
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = getKeyPairGenerator();
		keyPairGen.initialize(DEFAULT_KEY_SIZE, new SecureRandom());
		return keyPairGen.generateKeyPair();
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的公钥对象
	 *
	 * @param modulus        系数。
	 * @param publicExponent 专用指数。
	 * @return RSA专用公钥对象。
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static RSAPublicKey getRSAPublicKey(byte[] modulus, byte[] publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		KeyFactory keyFactory = getKeyFactory();
		return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的私钥对象
	 *
	 * @param modulus         系数。
	 * @param privateExponent 专用指数。
	 * @return RSA专用私钥对象。
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
				new BigInteger(privateExponent));
		KeyFactory keyFactory = getKeyFactory();
		return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象
	 *
	 * @param hexModulus         系数。
	 * @param hexPrivateExponent 专用指数。
	 * @return RSA专用私钥对象。
	 * @throws DecoderException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent)
			throws DecoderException, InvalidKeySpecException, NoSuchAlgorithmException {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPrivateExponent)) {
			return null;
		}
		byte[] modulus = Hex.decodeHex(hexModulus.toCharArray());
		byte[] privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		if (modulus != null && privateExponent != null) {
			return generateRSAPrivateKey(modulus, privateExponent);
		}
		return null;
	}
	
	public static String getPrivateExponent(KeyPair keyPair) {
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new String(Hex.encodeHex(privateKey.getPrivateExponent().toByteArray()));
	}
	
	public static String getPublicExponent(KeyPair keyPair) {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		return new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
	}
	
	public static String getModulus(KeyPair keyPair) {
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		return new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象
	 *
	 * @param hexModulus        系数。
	 * @param hexPublicExponent 专用指数。
	 * @return RSA专用公钥对象。
	 * @throws DecoderException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent)
			throws DecoderException, NoSuchAlgorithmException, InvalidKeySpecException {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPublicExponent)) {
			return null;
		}
		byte[] modulus = Hex.decodeHex(hexModulus.toCharArray());
		byte[] publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		if (modulus != null && publicExponent != null) {
			return getRSAPublicKey(modulus, publicExponent);
		}
		return null;
	}

	/**
	 * 使用指定的公钥加密数据
	 *
	 * @param publicKey 给定的公钥。
	 * @param data      要加密的数据。
	 * @return 加密后的数据。
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		ci.init(Cipher.ENCRYPT_MODE, publicKey);
		return ci.doFinal(data);
	}

	/**
	 * 使用指定的私钥解密数据
	 *
	 * @param privateKey 给定的私钥。
	 * @param data       要解密的数据。
	 * @return 原数据。
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		ci.init(Cipher.DECRYPT_MODE, privateKey);
		return ci.doFinal(data);
	}

	/**
	 * 使用给定的公钥加密给定的字符串
	 * <p />
	 * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null} 则返回
	 * {@code
	 * null}。
	 *
	 * @param publicKey 给定的公钥。
	 * @param plaintext 字符串。
	 * @return 给定字符串的密文。
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String encryptString(PublicKey publicKey, String plaintext) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		if (publicKey == null || plaintext == null) {
			return null;
		}
		byte[] data = plaintext.getBytes();
		byte[] encryptdata = encrypt(publicKey, data);
		return new String(Hex.encodeHex(encryptdata));
	}

	/**
	 * 使用给定的私钥解密给定的字符串
	 * <p />
	 * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
	 * 私钥不匹配时，返回 {@code null}。
	 *
	 * @param privateKey  给定的私钥
	 * @param encrypttext 密文
	 * @return 原文字符串。
	 * @throws DecoderException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String decryptString(PrivateKey privateKey, String encrypttext)
			throws DecoderException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		if (privateKey == null || StringUtils.isBlank(encrypttext)) {
			return null;
		}
		byte[] encryptdata = Hex.decodeHex(encrypttext.toCharArray());
		byte[] data = decrypt(privateKey, encryptdata);
		return new String(data);
	}

	/**
	 * 使用默认的私钥解密由JS加密（使用此类提供的公钥加密）的字符串
	 *
	 * @param privateKey  给定的私钥
	 * @param encrypttext 密文
	 * @return {@code encrypttext} 的原文字符串。
	 * @throws DecoderException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String decryptStringByJs(PrivateKey privateKey, String encrypttext)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, DecoderException {
		String text = decryptString(privateKey, encrypttext);
		if (text == null) {
			return null;
		}
		return StringUtils.reverse(text);
	}

	/**
	 * 签名
	 * 
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 */
	public static String sign(String content, RSAPrivateKey rsaPrivateKey) throws InvalidKeySpecException,
			NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
		KeyFactory keyFactory = getKeyFactory();
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded()));
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHOM);
		signature.initSign(privateKey);
		signature.update(content.getBytes(CHARSET_NAME));

		byte[] signed = signature.sign();
		return Hex.encodeHexString(signed);
	}

	/**
	 * 校验签名
	 * 
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws SignatureException
	 * @throws DecoderException
	 */
	public static boolean checkSign(String content, String sign, RSAPublicKey rsaPublicKey)
			throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, SignatureException,
			UnsupportedEncodingException, DecoderException {
		KeyFactory keyFactory = getKeyFactory();
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(rsaPublicKey.getEncoded()));
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHOM);
		signature.initVerify(publicKey);
		signature.update(content.getBytes(CHARSET_NAME));

		byte[] signatureByte = Hex.decodeHex(sign);
		return signature.verify(signatureByte);
	}

}