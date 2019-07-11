package org.smartframework.cloud.starter.mybatis.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.smartframework.cloud.starter.common.business.dto.BaseDto;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 多数据源配置
 * 
 * @author liyulin
 * @date 2019年5月25日 下午3:56:55
 * @since YamlShardingDataSourceFactory
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "smart")
public class SmartDatasourceProperties extends BaseDto {
	
	private static final long serialVersionUID = 1L;
	
	/** 多数据源配置信息 */
	private Map<String, SingleDatasourceProperties> datasources = new LinkedHashMap<>();
	/** sharding jdbc配置 */
	private Map<String, ShardingJdbcDatasourceProperties> shardingDatasources = new LinkedHashMap<>();
	
}