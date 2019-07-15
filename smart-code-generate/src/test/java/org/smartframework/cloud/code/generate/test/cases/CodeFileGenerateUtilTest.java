package org.smartframework.cloud.code.generate.test.cases;

import java.io.IOException;
import java.sql.SQLException;

import org.smartframework.cloud.code.generate.core.CodeGenerateUtil;

import junit.framework.TestCase;

public class CodeFileGenerateUtilTest extends TestCase {

	public void testInit() throws ClassNotFoundException, SQLException, IOException {
		CodeGenerateUtil.init();
	}
	
}