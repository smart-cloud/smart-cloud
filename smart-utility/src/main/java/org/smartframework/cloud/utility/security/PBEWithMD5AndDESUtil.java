package org.smartframework.cloud.utility.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * @author liyulin
 * @desc PBEWithMD5AndDES加解密工具类
 * @date 2020/02/20
 */
public class PBEWithMD5AndDESUtil {
    private static String ALGORITHM = "PBEWithHmacSHA256AndAES_128";
    private static PBEParameterSpec parameterSpec = null;

    static {
        // 初始化盐
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);
        parameterSpec = new PBEParameterSpec(salt, 256, new IvParameterSpec(new byte[16]));
    }

    private PBEWithMD5AndDESUtil() {
    }

    /**
     * 加密
     *
     * @param password 口令与密钥
     * @param word     待加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public static String encrypt(String password, String word)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        // 口令与密钥
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        Key key = factory.generateSecret(pbeKeySpec);

        // 加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptWord = cipher.doFinal(word.getBytes());
        return Base64.encodeBase64String(encryptWord);
    }

    /**
     * 解密
     *
     * @param password    口令与密钥
     * @param encryptWord 待解密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(String password, String encryptWord)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // 口令与密钥
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        Key key = factory.generateSecret(pbeKeySpec);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] decryptWord = cipher.doFinal(Base64.decodeBase64(encryptWord));
        return new String(decryptWord);
    }

}