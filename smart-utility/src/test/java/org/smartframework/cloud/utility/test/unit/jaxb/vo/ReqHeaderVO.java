package org.smartframework.cloud.utility.test.unit.jaxb.vo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * UIP请求报文节点SvcInfo——Header
 *
 * @author liyulin
 * @date 2018年12月13日下午10:12:37
 */
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Header")
public class ReqHeaderVO {

	@XmlElement(name = "TOKEN")
	private String token;

	@XmlElement(name = "TIMESTRAMP")
	private long timestramp;

}