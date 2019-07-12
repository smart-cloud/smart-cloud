package org.smartframework.cloud.utility.test.unit;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.JAXBUtil;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqBodyDto;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqDto;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqHeaderDto;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JAXBUtilUnitTest extends TestCase {

	public void test() throws JAXBException, IOException {
		ReqDto reqDto = new ReqDto(new ReqHeaderDto("123", System.currentTimeMillis()), new ReqBodyDto(123L, 456L));
		String xml = JAXBUtil.beanToXml(reqDto);
		ReqDto bean = JAXBUtil.xmlToBean(xml, ReqDto.class);
		Assertions.assertThat(JSON.toJSONString(reqDto)).isEqualTo(JSON.toJSONString(bean));
	}

}