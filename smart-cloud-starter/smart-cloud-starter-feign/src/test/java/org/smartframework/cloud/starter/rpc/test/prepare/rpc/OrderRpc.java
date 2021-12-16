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
package org.smartframework.cloud.starter.rpc.test.prepare.rpc;

import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.starter.rpc.feign.annotation.SmartFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SmartFeignClient(name = "orderRpc", url = "http://localhost:8080", contextId = "order")
public interface OrderRpc {

    @GetMapping("/order/queryWithJson")
    Response<String> queryWithJson(@RequestParam("id") Long id);

    @GetMapping("/order/rpc/queryWithProtobuf")
    Response<String> queryWithProtobuf(@RequestParam("id") Long id);

}