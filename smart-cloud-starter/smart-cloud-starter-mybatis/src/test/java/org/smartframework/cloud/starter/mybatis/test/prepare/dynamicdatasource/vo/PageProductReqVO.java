package org.smartframework.cloud.starter.mybatis.test.prepare.dynamicdatasource.vo;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.BasePageRequest;

/**
 * 分页查询商品信息请求参数
 *
 * @author liyulin
 * @date 2020-09-10
 */
@Getter
@Setter
public class PageProductReqVO extends BasePageRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

}