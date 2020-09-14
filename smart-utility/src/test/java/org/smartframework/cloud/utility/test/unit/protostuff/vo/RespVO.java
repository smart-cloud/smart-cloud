package org.smartframework.cloud.utility.test.unit.protostuff.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
public class RespVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 响应体
     */
    private T body;

}