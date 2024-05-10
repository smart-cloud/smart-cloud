/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.logback.test;

import io.github.smart.cloud.mask.MaskLog;
import io.github.smart.cloud.mask.MaskRule;
import io.github.smart.cloud.mask.util.MaskUtil;
import io.github.smart.cloud.starter.logback.test.prepare.Application;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class MaskMessageConverterTest {

    @Test
    void test() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        User user = new User();
        user.setId(9L);
        user.setName("zhangsan");
        user.setMobile("13112345678");
        log.info("user={}", user);

        // 判断日志中是否脱敏
        String maskName = MaskUtil.mask(user.getName(), MaskRule.NAME);
        String maskMobile = MaskUtil.mask(user.getMobile(), MaskRule.MOBILE);
        String logContent = output.toString();
        Assertions.assertThat(logContent).contains(maskName, maskMobile);
    }

    @Getter
    @Setter
    @ToString
    static class User implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        @MaskLog(MaskRule.NAME)
        private String name;
        @MaskLog(MaskRule.MOBILE)
        private String mobile;
    }

}