package org.smartframework.cloud.utility.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

import lombok.experimental.UtilityClass;

/**
 * md5工具类
 * 
 * @author liyulin
 * @date 2019-07-12
 */
@UtilityClass
public class Md5Util {

	/**
	 * 生成文件的md5
	 * 
	 * @param path 文件路径
	 * @return
	 * @throws IOException
	 */
	public String generateFileMd5(String path) throws IOException {
		return generateFileMd5(new File(path));
	}

	/**
	 * 生成文件的md5
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String generateFileMd5(File file) throws IOException {
		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			return DigestUtils.md5Hex(fileInputStream);
		}
	}

}