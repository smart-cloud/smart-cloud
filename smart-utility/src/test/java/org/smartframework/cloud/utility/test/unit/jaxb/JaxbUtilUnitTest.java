package org.smartframework.cloud.utility.test.unit.jaxb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.JaxbUtil;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqBodyVO;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqVO;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqHeaderVO;

import javax.xml.bind.JAXBException;
import java.io.IOException;

class JaxbUtilUnitTest {

    @Test
    void test() throws JAXBException, IOException {
        ReqVO reqVO = new ReqVO(new ReqHeaderVO("123", System.currentTimeMillis()), new ReqBodyVO(123L, 456L));
        String xml = JaxbUtil.beanToXml(reqVO);
        ReqVO bean = JaxbUtil.xmlToBean(xml, ReqVO.class);
        Assertions.assertThat(JacksonUtil.toJson(reqVO)).isEqualTo(JacksonUtil.toJson(bean));
    }

}