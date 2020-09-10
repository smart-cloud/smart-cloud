package org.smartframework.cloud.common.pojo.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.smartframework.cloud.common.pojo.Base;
import org.smartframework.cloud.common.pojo.enums.IBaseReturnCode;
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
    @PodamStringValue(strValue = "100200")
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应时间戳
     */
    private long timestamp;

    public RespHeadVO(IBaseReturnCode returnCode) {
        setReturnCode(returnCode);
    }

    public RespHeadVO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RespHeadVO(IBaseReturnCode returnCode, String message) {
        if (returnCode != null) {
            this.code = returnCode.getCode();
        }
        this.message = message;
    }

    public void setReturnCode(IBaseReturnCode returnCode) {
        if (returnCode == null) {
            return;
        }

        this.code = returnCode.getCode();
        this.message = returnCode.getMessage();
    }

}