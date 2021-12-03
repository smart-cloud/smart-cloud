/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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