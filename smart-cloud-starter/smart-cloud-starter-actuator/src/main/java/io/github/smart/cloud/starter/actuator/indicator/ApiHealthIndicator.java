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