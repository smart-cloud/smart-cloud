package org.smartframework.cloud.common.pojo.bo;

import lombok.*;
import org.smartframework.cloud.common.pojo.Base;

/**
 * boolean BO
 *
 * @author liyulin
 * @date 2019-07-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BooleanResultBO extends Base {

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功（通过）
     */
    private Boolean success;

    /**
     * 提示信息
     */
    private String message;

    public BooleanResultBO(boolean success) {
        this.success = success;
    }

}