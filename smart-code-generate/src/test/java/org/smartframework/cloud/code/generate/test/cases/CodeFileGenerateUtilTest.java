package org.smartframework.cloud.code.generate.test.cases;

import net.sf.jsqlparser.JSQLParserException;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.code.generate.core.CodeGenerateUtil;

import java.io.IOException;
import java.sql.SQLException;

public class CodeFileGenerateUtilTest {

    @Test
    public void testInit() throws ClassNotFoundException, SQLException, IOException, JSQLParserException {
        CodeGenerateUtil.init();
    }

}