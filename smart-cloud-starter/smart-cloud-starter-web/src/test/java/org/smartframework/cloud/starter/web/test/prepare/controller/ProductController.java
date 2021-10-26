package org.smartframework.cloud.starter.web.test.prepare.controller;

import org.smartframework.cloud.common.pojo.Response;
import org.smartframework.cloud.starter.core.business.util.RespUtil;
import org.smartframework.cloud.starter.web.test.prepare.vo.ProductCreateReqVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("product")
public class ProductController {

    @GetMapping
    public Response<String> query(@NotNull Long id) {
        return RespUtil.success(String.format("iphone_%s", id));
    }

    @PostMapping
    public Response<Boolean> create(@RequestBody @Valid ProductCreateReqVO reqVO) {
        return RespUtil.success(true);
    }

}