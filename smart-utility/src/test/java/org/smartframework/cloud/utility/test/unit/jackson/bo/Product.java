package org.smartframework.cloud.utility.test.unit.jackson.bo;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

}