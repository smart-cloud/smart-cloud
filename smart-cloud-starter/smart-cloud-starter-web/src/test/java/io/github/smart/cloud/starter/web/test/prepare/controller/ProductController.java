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
package io.github.smart.cloud.starter.web.test.prepare.controller;

import io.github.smart.cloud.common.pojo.Response;
import io.github.smart.cloud.starter.core.util.ResponseUtil;
import io.github.smart.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping
    public Response<String> query(@NotNull Long id, HttpServletRequest request) {
        return ResponseUtil.success(String.format("iphone_%s", id));
    }

    @PostMapping
    public Response<Boolean> create(@RequestBody @Valid ProductCreateReqVO reqVO) {
        return ResponseUtil.success(true);
    }

}