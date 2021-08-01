package org.smartframework.cloud.starter.mock.test.prepare.vo;

import lombok.*;
import uk.co.jemos.podam.common.PodamBooleanValue;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubmitOrderResponse {

    @PodamBooleanValue(boolValue = true)
    private Boolean result;

}