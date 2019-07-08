package org.smartframework.cloud.utility.test.unit;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.assertj.core.api.Assertions;
import org.smartframework.cloud.utility.JAXBUtil;
import org.smartframework.cloud.utility.test.unit.jaxb.ReqDto;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

public class JAXBUtilUnitTest extends TestCase {

	public void test() throws JAXBException, IOException {
		ReqDto reqDto = new ReqDto();
		String xml = JAXBUtil.beanToXml(reqDto);
		ReqDto bean = JAXBUtil.xmlToBean(xml, ReqDto.class);
		Assertions.assertThat(JSON.toJSONString(reqDto)).isEqualTo(JSON.toJSONString(bean));
	}

}