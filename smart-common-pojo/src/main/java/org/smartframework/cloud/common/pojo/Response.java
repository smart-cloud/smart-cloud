package org.smartframework.cloud.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.smartframework.cloud.common.pojo.enums.CommonReturnCodes;

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
public class Response<T> extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 响应头部
     */
    private ResponseHead head = null;

    /**
     * 响应体
     */
    private T body;

    /**
     * 签名
     */
    private String sign;

    public Response(ResponseHead head) {
        this.head = head;
    }

    public Response(T body) {
        this.head = new ResponseHead(CommonReturnCodes.SUCCESS);
        this.body = body;
    }

}