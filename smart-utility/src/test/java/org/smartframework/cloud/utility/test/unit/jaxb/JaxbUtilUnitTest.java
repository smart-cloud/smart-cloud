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
package org.smartframework.cloud.utility.test.unit.jaxb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.utility.JaxbUtil;
import org.smartframework.cloud.utility.JacksonUtil;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqBodyVO;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqVO;
import org.smartframework.cloud.utility.test.unit.jaxb.vo.ReqHeaderVO;

import javax.xml.bind.JAXBException;
import java.io.IOException;

class JaxbUtilUnitTest {

    @Test
    void test() throws JAXBException, IOException {
        ReqVO reqVO = new ReqVO(new ReqHeaderVO("123", System.currentTimeMillis()), new ReqBodyVO(123L, 456L));
        String xml = JaxbUtil.beanToXml(reqVO);
        ReqVO bean = JaxbUtil.xmlToBean(xml, ReqVO.class);
        Assertions.assertThat(JacksonUtil.toJson(reqVO)).isEqualTo(JacksonUtil.toJson(bean));
    }

}