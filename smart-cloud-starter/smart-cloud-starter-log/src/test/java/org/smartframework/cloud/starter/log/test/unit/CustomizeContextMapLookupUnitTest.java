package org.smartframework.cloud.starter.log.test.unit;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.starter.log.log4j2.plugin.CustomizeContextMapLookup;

import junit.framework.TestCase;

public class CustomizeContextMapLookupUnitTest extends TestCase {

	public void testReadAppName() {
		CustomizeContextMapLookup customizeContextMapLookup = new CustomizeContextMapLookup();
		
		Assertions.assertThat(customizeContextMapLookup.lookup("appName")).isEqualTo("smart-cloud-starter-log");
	}
	
}