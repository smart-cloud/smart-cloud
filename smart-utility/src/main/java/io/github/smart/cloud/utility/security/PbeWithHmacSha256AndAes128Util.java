/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.utility.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * PBEWithHmacSHA256AndAES_128加解密工具类
 *
 * @author collin
 * @date 2020/02/20
 */
public class PbeWithHmacSha256AndAes128Util {
    private static final String ALGORITHM = "PBEWithHmacSHA256AndAES_128";
    private static PBEParameterSpec parameterSpec = null;

    static {
        // 初始化盐
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);
        parameterSpec = new PBEParameterSpec(salt, 256, new IvParameterSpec(new byte[16]));
    }

    private PbeWithHmacSha256AndAes128Util() {
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