package io.github.smart.cloud.starter.staticdiscovery.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class StaticDiscoveryProperties {

    public static final String PREFIX = "smart.cloud.static-discovery";

    private Map<String, List<String>> instanceConfig;

}