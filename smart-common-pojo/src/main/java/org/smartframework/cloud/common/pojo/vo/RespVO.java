package org.smartframework.cloud.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.enums.ReturnCodeEnum;

/**
 * 响应对象
 *
 * @author liyulin
 * @date 2020-05-07
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RespVO<T> extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 响应头部
     */
    private RespHeadVO head = null;

    /**
     * 响应体
     */
    private T body;

    /**
     * 签名
     */
    private String sign;

    public RespVO(RespHeadVO head) {
        this.head = head;
    }

    public RespVO(T body) {
        this.head = new RespHeadVO(ReturnCodeEnum.SUCCESS);
        this.body = body;
    }

}