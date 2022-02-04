package org.smartframework.cloud.starter.redis.test.prepare.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class User implements Serializable {

    private Long id;
    private String mobile;

}
