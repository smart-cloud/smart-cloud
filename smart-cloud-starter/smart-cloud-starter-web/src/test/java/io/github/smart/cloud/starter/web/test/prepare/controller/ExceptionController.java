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
import io.github.smart.cloud.starter.core.business.util.ResponseUtil;
import io.github.smart.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 空指针异常
 *
 * @author collin
 * @date 2021-12-16
 */
@Validated
@RestController
@RequestMapping("exception")
public class ExceptionController {

    @GetMapping("bind")
    public Response<Boolean> bind(@Valid @NotNull ProductCreateReqVO reqVO) {
        return ResponseUtil.success(true);
    }

    @GetMapping("bind2")
    public Response<Boolean> bind2(@NotBlank String name) {
        return ResponseUtil.success(true);
    }


    @GetMapping(value = "mediaTypeNotSupported", consumes = "application/rss+xml")
    public Response<Boolean> xml(@Valid @NotNull ProductCreateReqVO reqVO) {
        return ResponseUtil.success(true);
    }

    @PostMapping("mismatchedInputException")
    public Response<Boolean> mismatchedInputException(@RequestBody @Valid @NotNull ProductCreateReqVO reqVO) {
        return ResponseUtil.success(true);
    }

    @GetMapping("other")
    public Response<Boolean> other() {
        throw new NullPointerException("test");
    }

}