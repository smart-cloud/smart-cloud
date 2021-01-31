package org.smartframework.cloud.common.pojo.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCodes;
import uk.co.jemos.podam.common.PodamStringValue;

/**
 * 响应头部
 *
 * @author liyulin
 * @date 2020-05-07
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RespHeadVO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 全局唯一交易流水号
     */
    private String nonce;

    /**
     * 响应状态码
     */
    @PodamStringValue(strValue = "200")
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应时间戳
     */
    private long timestamp;

    public RespHeadVO(IBaseReturnCodes returnCodes) {
        setReturnCode(returnCodes);
    }

    public RespHeadVO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setReturnCode(IBaseReturnCodes returnCodes) {
        if (returnCodes == null) {
            return;
        }

        this.code = returnCodes.getCode();
    }

}