package org.smartframework.cloud.starter.mock.test.prepare.vo;

import lombok.*;
import uk.co.jemos.podam.common.PodamBooleanValue;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeductDTO {

    @PodamBooleanValue(boolValue = false)
    private Boolean result;

}