package org.smartframework.cloud.starter.core.business.security.bo;

import lombok.Getter;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;

/**
 * 响应信息体（加密+签名）
 *
 * @author liyulin
 * @date 2020-09-10
 */
@Getter
@Setter
public class RespBO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 加密后的head
     */
    private String head;
    /**
     * 加密后的body
     */
    private String body;
    /**
     * 签名
     */
    private String sign;

}