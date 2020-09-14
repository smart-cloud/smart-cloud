package org.smartframework.cloud.utility.test.unit.protostuff.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private long price;
    private String name;

}