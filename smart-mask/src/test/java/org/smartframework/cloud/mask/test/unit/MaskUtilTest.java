package org.smartframework.cloud.mask.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.mask.util.MaskUtil;

import junit.framework.TestCase;

public class MaskUtilTest extends TestCase {

	public void testMask() {
		Assertions.assertThat(MaskUtil.mask(null, 3, 3, "***")).isEqualTo("***");
		Assertions.assertThat(MaskUtil.mask("", 3, 3, "***")).isEqualTo("***");
		Assertions.assertThat(MaskUtil.mask("13112345678", 11, 3, "***")).isEqualTo("***");
		Assertions.assertThat(MaskUtil.mask("13112345678", 3, 10, "***")).isEqualTo("131***");
		Assertions.assertThat(MaskUtil.mask("13112345678", 3, 3, "***")).isEqualTo("131***678");
	}

}