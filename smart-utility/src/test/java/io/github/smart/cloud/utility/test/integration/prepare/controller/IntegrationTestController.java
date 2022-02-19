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
package io.github.smart.cloud.utility.test.integration.prepare.controller;

import io.github.smart.cloud.utility.test.integration.prepare.vo.GetPageReqVO;
import io.github.smart.cloud.utility.test.integration.prepare.vo.GetPageRespVO;
import io.github.smart.cloud.utility.test.integration.prepare.vo.PostUrlEncodedReqVO;
import io.github.smart.cloud.utility.test.integration.prepare.vo.PostUrlEncodedRespVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class IntegrationTestController {

    @GetMapping
    public String get(String str) {
        return str;
    }

    @GetMapping("page")
    public GetPageRespVO page(GetPageReqVO reqVO) {
        return new GetPageRespVO(reqVO.getStr(), reqVO.getIds());
    }

    @PostMapping
    public String post(@RequestBody String str) {
        return str;
    }

    @PostMapping("list")
    public List<String> list(@RequestBody String str) {
        List<String> list = new ArrayList<>();
        list.add(str);
        return list;
    }

    @PostMapping("postUrlEncoded")
    public PostUrlEncodedRespVO postUrlEncoded(PostUrlEncodedReqVO req) {
        return new PostUrlEncodedRespVO(req.getId());
    }

}