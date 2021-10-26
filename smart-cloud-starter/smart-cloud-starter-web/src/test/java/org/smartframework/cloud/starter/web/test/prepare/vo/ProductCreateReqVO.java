package org.smartframework.cloud.starter.web.test.prepare.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.smartframework.cloud.common.pojo.Base;

import javax.validation.constraints.NotBlank;

/**
 * 创建产品
 *
 * @author collin
 * @date 2021-10-28
 */
@Getter
@Setter
public class ProductCreateReqVO extends Base {

    @NotBlank
    @Length(max = 64)
    private String name;

    @NotBlank
    @Length(max = 128)
    private String desc;

}