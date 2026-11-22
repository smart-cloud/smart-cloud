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
package io.github.smart.cloud.starter.log4j2.test.cases;

import io.github.smart.cloud.starter.log.mask.pattern.PatternConfig;
import io.github.smart.cloud.starter.log.mask.pattern.enums.MaskMode;
import io.github.smart.cloud.starter.log.mask.pattern.util.LogMaskContext;
import io.github.smart.cloud.starter.log4j2.test.prepare.App;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@Slf4j
@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MaskTest {

    /**
     * 自定义正则表达式
     */
    public static final String REGEX = "\\s*((?:\\\\\"|[\"'])?[(姓名|mobile|公司)]+(?:\\\\\"|[\"'])?)(\\s*[:：=]+)\\s*((?:\\\\\"|[\"'])?[^,\\[\\]{};，；\\s]+(?:\\\\\"|[\"'])?)\\s*";

    @Test
    public void testOff() {
        PatternConfig.setMode(MaskMode.OFF.getValue());
        try {
            LogMaskContext.set(REGEX);
            String name = "张三";
            String mobile = "1312134454";
            log.info("XXX,姓名:{},测试", name);
            log.info("XXX,mobile:{},测试", mobile);
            log.info("xx，姓名:{}, mobile= {}.测试", name, mobile);
        } finally {
            LogMaskContext.remove();
        }
    }

    @Test
    public void testAnnotation(CapturedOutput output) {
        PatternConfig.setMode(MaskMode.ANNOTATION.getValue());
        try {
            LogMaskContext.set(REGEX);
            String name = "张三";
            String mobile = "1312134454";
            String company = "少时诵诗书";

            log.info("XXX,姓名:{},测试", name);
            Assertions.assertThat(output.getAll()).contains("XXX,姓名:***,测试");

            log.info("XXX,mobile:{},测试", mobile);
            Assertions.assertThat(output.getAll()).contains("XXX,mobile:***,测试");

            log.info("xx，姓名:{}, mobile= {}.测试,公司={}", name, mobile, company);
            Assertions.assertThat(output.getAll()).contains("xx，姓名:***,mobile=***,公司=***");
        } finally {
            LogMaskContext.remove();
        }
    }

    @Test
    public void testFull(CapturedOutput output) {
        PatternConfig.setMode(MaskMode.FULL.getValue());
        String name = "张三";
        String mobile = "1312134454";

        log.info("XXX,姓名:{},测试", name);
        Assertions.assertThat(output.getAll()).contains("XXX,姓名:***,测试");

        log.info("XXX,mobile:{},测试", mobile);
        Assertions.assertThat(output.getAll()).contains("XXX,mobile:***,测试");

        log.info("xx，姓名:{}, mobile= {}.测试 GG", name, mobile);
        Assertions.assertThat(output.getAll()).contains("xx，姓名:***, mobile= *** GG");
    }

}