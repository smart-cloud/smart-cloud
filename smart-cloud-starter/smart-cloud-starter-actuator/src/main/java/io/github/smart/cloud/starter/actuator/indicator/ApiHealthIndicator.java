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
package io.github.smart.cloud.starter.actuator.indicator;

import io.github.smart.cloud.starter.actuator.dto.UnHealthApiDTO;
import io.github.smart.cloud.starter.actuator.repository.ApiHealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口健康信息检测
 *
 * @author collin
 * @date 2024-01-6
 */
@RequiredArgsConstructor
public class ApiHealthIndicator extends AbstractHealthIndicator {

    private final ApiHealthRepository apiRepository;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        List<UnHealthApiDTO> unHealthInfos = apiRepository.getUnHealthInfos();
        if (unHealthInfos.isEmpty()) {
            builder.up();
        } else {
            Map<String, List<UnHealthApiDTO>> details = new HashMap<>(1);
            details.put("unHealthInfos", unHealthInfos);

            builder.down().withDetails(details);
        }
    }

}