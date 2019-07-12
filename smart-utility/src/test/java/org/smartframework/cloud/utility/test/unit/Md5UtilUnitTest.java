package org.smartframework.cloud.utility.test.unit;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.security.Md5Util;

import junit.framework.TestCase;

public class Md5UtilUnitTest extends TestCase {

	public void testGenerateFileMd5() throws IOException {
		String path = Md5UtilUnitTest.class.getResource("").getFile() + Md5UtilUnitTest.class.getSimpleName()
				+ ".class";

		String md5 = Md5Util.generateFileMd5(path);
		Assertions.assertThat(md5).isNotBlank();
	}

}