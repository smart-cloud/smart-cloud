package org.smartframework.cloud.starter.log.test.unit;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.mask.MaskLog;
import org.smartframework.cloud.mask.MaskRule;

import java.io.Serializable;

@Slf4j
public class MaskTest {

    @Test
    public void testMask() {
        User user = new User();
        user.setId(9L);
        user.setName("名字");
        user.setMobile("13112345678");
        log.info("user={}", user);
    }

    @Getter
    @Setter
    @ToString
    public static class User implements Serializable {
        private Long id;
        @MaskLog(MaskRule.NAME)
        private String name;
        @MaskLog(MaskRule.MOBILE)
        private String mobile;
    }

}