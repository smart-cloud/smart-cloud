package org.smartframework.cloud.utility.test.unit.jaxb.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * UIP请求报文
 *
 * @author liyulin
 * @date 2018年12月13日下午10:12:14
 */
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "INFO")
public class ReqVO {

	@XmlElement(name = "HEADER")
	private ReqHeaderVO header;

	@XmlElement(name = "BODY")
	private ReqBodyVO body;

}