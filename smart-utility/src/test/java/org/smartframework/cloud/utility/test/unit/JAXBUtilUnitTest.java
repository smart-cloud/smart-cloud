package org.smartframework.cloud.utility.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.JAXBUtil;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqBodyDto;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqDto;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqHeaderDto;

import javax.xml.bind.JAXBException;
import java.io.IOException;

class JAXBUtilUnitTest {

    @Test
    void test() throws JAXBException, IOException {
        ReqDto reqDto = new ReqDto(new ReqHeaderDto("123", System.currentTimeMillis()), new ReqBodyDto(123L, 456L));
        String xml = JAXBUtil.beanToXml(reqDto);
        ReqDto bean = JAXBUtil.xmlToBean(xml, ReqDto.class);
        Assertions.assertThat(JacksonUtil.toJson(reqDto)).isEqualTo(JacksonUtil.toJson(bean));
    }

}