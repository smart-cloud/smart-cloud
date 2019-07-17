package org.smartframework.cloud.starter.mybatis.properties;

import java.util.LinkedHashMap;
import java.util.Map;

import org.smartframework.cloud.common.pojo.dto.BaseDto;
import org.smartframework.cloud.starter.mybatis.enums.ToolTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 多数据源配置
 * 
 * @author liyulin
 * @date 2019-05-25
 * @since YamlShardingDataSourceFactory
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "smart")
public class SmartDatasourceProperties extends BaseDto {

	private static final long serialVersionUID = 1L;

	/** 集成的工具类型（1.mapper（默认）：通用mapper。2.mybatisplus） */
	private String toolType = ToolTypeEnum.MAPPER.getType();
	/** 多数据源配置信息 */
	private Map<String, SingleDatasourceProperties> datasources = new LinkedHashMap<>();
	/** sharding jdbc配置 */
	private Map<String, ShardingJdbcDatasourceProperties> shardingDatasources = new LinkedHashMap<>();

}