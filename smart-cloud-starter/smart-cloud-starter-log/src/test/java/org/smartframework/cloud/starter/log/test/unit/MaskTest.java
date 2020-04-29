package org.smartframework.cloud.starter.log.test.unit;

import org.smartframework.cloud.mask.EnableMask;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MaskTest  extends TestCase {

	public void testMask() {
		User user = new User();
		user.setId(9L);
		user.setName("名字");
		user.setMobile("13112345678");
		log.info("user={}", user);
	}
	
	@Getter
	@Setter
	@ToString
	@EnableMask
	public static class User {
		private Long id;
		@MaskLog(MaskRule.NAME)
		private String name;
		@MaskLog(MaskRule.MOBILE)
		private String mobile;
	}
	
}