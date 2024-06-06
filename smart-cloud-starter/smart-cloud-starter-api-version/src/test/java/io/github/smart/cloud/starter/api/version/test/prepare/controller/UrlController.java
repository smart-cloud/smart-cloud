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
package io.github.smart.cloud.starter.api.version.test.prepare.controller;

import io.github.smart.cloud.starter.api.version.core.ApiHandlerFactory;
import io.github.smart.cloud.starter.api.version.test.prepare.constants.ApiHandlerNames;
import io.github.smart.cloud.starter.api.version.test.prepare.vo.ApiVersionDemoRequestVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * url匹配
 *
 * @author collin
 * @date 2024-05-30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("client/url")
public class UrlController {

    private final ApiHandlerFactory apiHandlerFactory;

    @PostMapping(value = "/{version:v\\d+}/test")
    public String testV2(@PathVariable(name = "version") String version, @RequestBody ApiVersionDemoRequestVO requestVO) {
        return apiHandlerFactory.handle(ApiHandlerNames.API_VERSION_DEMO_API_HANDLER, version, new Object[]{requestVO});
    }

}