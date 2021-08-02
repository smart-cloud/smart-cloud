package org.smartframework.cloud.utility.test.integration.prepare.controller;

import org.smartframework.cloud.utility.test.integration.prepare.vo.GetPageReqVO;
import org.smartframework.cloud.utility.test.integration.prepare.vo.GetPageRespVO;
import org.smartframework.cloud.utility.test.integration.prepare.vo.PostUrlEncodedReqVO;
import org.smartframework.cloud.utility.test.integration.prepare.vo.PostUrlEncodedRespVO;
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