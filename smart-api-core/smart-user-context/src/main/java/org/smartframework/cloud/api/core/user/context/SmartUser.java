package org.smartframework.cloud.api.core.user.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户信息公共字段
 *
 * @author collin
 * @date 2021-03-03
 */
@Getter
@Setter
@ToString
public class SmartUser implements Serializable {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

}