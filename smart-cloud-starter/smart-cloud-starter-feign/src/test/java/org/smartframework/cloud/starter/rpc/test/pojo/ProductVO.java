package org.smartframework.cloud.starter.rpc.test.pojo;

import org.smartframework.cloud.common.pojo.Base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ProductVO extends Base{

	private static final long serialVersionUID = 1L;
	private long id;
	private long price;
	private String name;
	
}