package org.smartframework.cloud.utility.security;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 *
 * @author liyulin
 * @date 2019-07-12
 */
public class Md5Util extends DigestUtils {

    /**
     * Creates an instance using the provided {@link MessageDigest} parameter.
     * <p>
     * This can then be used to create digests using methods such as
     * {@link #digest(byte[])} and {@link #digestAsHex(File)}.
     *
     * @param digest the {@link MessageDigest} to use
     * @since 1.11
     */
    public Md5Util(final MessageDigest digest) {
        super(digest);
    }

    /**
     * Creates an instance using the provided {@link MessageDigest} parameter.
     * <p>
     * This can then be used to create digests using methods such as
     * {@link #digest(byte[])} and {@link #digestAsHex(File)}.
     *
     * @param name the name of the {@link MessageDigest} to use
     * @throws IllegalArgumentException when a {@link NoSuchAlgorithmException} is caught.
     * @see #getDigest(String)
     * @since 1.11
     */
    public Md5Util(final String name) {
        super(name);
    }

    /**
     * 生成文件的md5
     *
     * @param path 文件路径
     * @return
     * @throws IOException
     */
    public static String generateFileMd5(String path) throws IOException {
        return generateFileMd5(new File(path));
    }

    /**
     * 生成文件的md5
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String generateFileMd5(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fileInputStream);
        }
    }

}