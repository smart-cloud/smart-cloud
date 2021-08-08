package org.smartframework.cloud.utility.test.unit.jaxb.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class ReqBodyVO {

	@XmlElement(name = "ID")
	private Long id;
	
	@XmlElement(name = "PRICE")
	private Long price;

}