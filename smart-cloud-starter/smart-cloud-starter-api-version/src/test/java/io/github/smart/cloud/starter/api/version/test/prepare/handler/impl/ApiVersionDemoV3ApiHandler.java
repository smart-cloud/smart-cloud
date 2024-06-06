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
package io.github.smart.cloud.starter.api.version.test.prepare.handler.impl;

import io.github.smart.cloud.starter.api.version.annotation.ApiHandlerMethod;
import io.github.smart.cloud.starter.api.version.annotation.ApiHandlerVersion;
import io.github.smart.cloud.starter.api.version.test.prepare.constants.ApiHandlerNames;
import io.github.smart.cloud.starter.api.version.test.prepare.vo.ApiVersionDemoRequestVO;
import org.springframework.stereotype.Component;

@Component
@ApiHandlerVersion(routeKeyPrefix = ApiHandlerNames.API_VERSION_DEMO_API_HANDLER, version = 3)
public class ApiVersionDemoV3ApiHandler {

    @ApiHandlerMethod
    public String process(ApiVersionDemoRequestVO apiVersionDemoRequestVO) {
        return apiVersionDemoRequestVO.getName() + "v3";
    }

}