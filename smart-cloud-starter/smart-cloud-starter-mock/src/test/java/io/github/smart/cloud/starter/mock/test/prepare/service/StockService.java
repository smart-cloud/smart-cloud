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
package io.github.smart.cloud.starter.mock.test.prepare.service;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.starter.mock.annotation.Mock;
import io.github.smart.cloud.starter.mock.test.prepare.vo.DeductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockService {

    /**
     * 走mock
     *
     * @return
     */
    @Mock
    public DeductDTO deduct() {
        return new DeductDTO(true);
    }

    /**
     * 泛型
     *
     * @return
     */
    @Mock
    public Response<List<DeductDTO>> list() {
        return null;
    }

    /**
     * 白名单
     *
     * @return
     */
    @Mock
    public DeductDTO query() {
        return new DeductDTO(true);
    }

}