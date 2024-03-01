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
package io.github.smart.cloud.starter.rate.limit.test.prepare.controller;

import io.github.smart.cloud.starter.rate.limit.annotation.RateLimit;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping
    public String query() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "张三";
    }

    @RateLimit(permits = 1)
    @PostMapping
    public Boolean create() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return true;
    }

    @PutMapping
    public Boolean update() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return true;
    }

}