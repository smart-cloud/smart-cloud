package org.smartframework.cloud.utility.test.unit.protostuff;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.SerializingUtil;
import org.smartframework.cloud.utility.test.unit.protostuff.vo.OrderVO;
import org.smartframework.cloud.utility.test.unit.protostuff.vo.ProductVO;
import org.smartframework.cloud.utility.test.unit.protostuff.vo.RespVO;

import java.io.ByteArrayInputStream;
import java.io.IOException;

class SerializingUtilUnitTest {

    @Test
    void testDeserialize() throws IOException {
        RespVO<OrderVO> ordeRespVO = new RespVO<>();
        ordeRespVO.setBody(OrderVO.builder().orderNo("1234123").build());

        RespVO<ProductVO> productRespVO = new RespVO<>();
        productRespVO.setBody(ProductVO.builder().id(123).name("ddd").build());


        byte[] orderRespVOByte = SerializingUtil.serialize(ordeRespVO);
        RespVO<OrderVO> ordeRespVO2 = SerializingUtil.deserialize(new ByteArrayInputStream(orderRespVOByte), RespVO.class);
        Assertions.assertThat(ordeRespVO2.getBody().getOrderNo()).isEqualTo(ordeRespVO.getBody().getOrderNo());

        // 字节流入参
        byte[] productRespVOByte = SerializingUtil.serialize(productRespVO);
        RespVO<ProductVO> productRespVO2 = SerializingUtil.deserialize(new ByteArrayInputStream(productRespVOByte), RespVO.class);
        Assertions.assertThat(productRespVO2.getBody().getId()).isEqualTo(productRespVO.getBody().getId());
        Assertions.assertThat(productRespVO2.getBody().getName()).isEqualTo(productRespVO.getBody().getName());

        // 字节数组入参
        RespVO<ProductVO> productRespVO3 = SerializingUtil.deserialize(productRespVOByte, RespVO.class);
        Assertions.assertThat(productRespVO3.getBody().getId()).isEqualTo(productRespVO.getBody().getId());
        Assertions.assertThat(productRespVO3.getBody().getName()).isEqualTo(productRespVO.getBody().getName());
    }

}