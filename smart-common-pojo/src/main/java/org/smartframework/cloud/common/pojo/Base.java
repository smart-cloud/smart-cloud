package org.smartframework.cloud.common.pojo;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * pojo基类
 *
 * @author liyulin
 * @date 2019-04-22
 */
@NoArgsConstructor
@SuperBuilder
public class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}