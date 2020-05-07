package org.smartframework.cloud.starter.rpc.test.unit;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.smartframework.cloud.common.pojo.vo.RespVO;
import org.smartframework.cloud.starter.rpc.feign.protostuff.SerializingUtil;
import org.smartframework.cloud.starter.rpc.test.pojo.OrderVO;
import org.smartframework.cloud.starter.rpc.test.pojo.ProductVO;

import junit.framework.TestCase;

public class SerializingUtilUnitTest extends TestCase {

	@Test
	@SuppressWarnings("unchecked")
	public void testDeserialize() throws IOException {
		RespVO<OrderVO> ordeRespVO = new RespVO<>();
		ordeRespVO.setBody(OrderVO.builder().orderNo("1234123").build());
		

		RespVO<ProductVO> productRespVO = new RespVO<>();
		productRespVO.setBody(ProductVO.builder().id(123).name("ddd").build());

		
		byte[]  orderRespVOByte = SerializingUtil.serialize(ordeRespVO);
		RespVO<OrderVO> ordeRespVO2 = SerializingUtil.deserialize(new ByteArrayInputStream(orderRespVOByte), RespVO.class);
		Assertions.assertThat(ordeRespVO2.getBody().getOrderNo()).isEqualTo(ordeRespVO.getBody().getOrderNo());
		
		byte[]  productRespVOByte = SerializingUtil.serialize(productRespVO);
		RespVO<ProductVO> productRespVO2 = SerializingUtil.deserialize(new ByteArrayInputStream(productRespVOByte), RespVO.class);
		Assertions.assertThat(productRespVO2.getBody().getId()).isEqualTo(productRespVO.getBody().getId());
		Assertions.assertThat(productRespVO2.getBody().getName()).isEqualTo(productRespVO.getBody().getName());
	}
	
}