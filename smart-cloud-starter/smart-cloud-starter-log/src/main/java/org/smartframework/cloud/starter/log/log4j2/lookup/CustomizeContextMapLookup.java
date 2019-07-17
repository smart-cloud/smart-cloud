package org.smartframework.cloud.starter.log.log4j2.lookup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.smartframework.cloud.starter.log.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * 自定义lo4j2插件，用于设置变量
 * 
 * <p>NOTE：需要在log4j2的配置文件中设置“packages”的属性值（即当前类的包名）
 *
 * @author liyulin
 * @date 2019-03-19
 */
@Plugin(name = "cctx", category = StrLookup.CATEGORY)
public class CustomizeContextMapLookup implements StrLookup {
	
	/** yaml文件名 */
	private static final String YAML_FILE_NAME = "application.yml";
	/** spring yaml文件中key的分隔符 */
	private static final String SPRING_YAML_KEY_SEPARATOR = "\\.";
	/** yaml文件中的应用名key */
	private static final String APP_NAME_KEY = "spring.application.name";
	/** 存储设置的变量 */
	private static final Map<String, String> DATA = new HashMap<>();

	static {
		// 解析yaml
		ClassPathResource resource = new ClassPathResource(YAML_FILE_NAME);
		String appName = null;
		if (!resource.exists()) {
			// 不存在，则取当前工程名
			appName = getCurrentProjectName();
		} else {
			appName = readAppNameFromYaml(resource);
			if (StringUtils.isEmpty(appName)) {
				appName = getCurrentProjectName();
			}
		}
		
		DATA.put("appName", appName);
	}
	
	/**
	 * 获取当前工程名
	 * 
	 * @return
	 */
	private static String getCurrentProjectName() {
		ApplicationHome applicationHome = new ApplicationHome(CustomizeContextMapLookup.class);
		File dir = applicationHome.getDir();
		if (dir == null) {
			return null;
		}
		return dir.getName();
	}
	
	/**
	 * 从yaml文件中读取服务名
	 * 
	 * @param resource
	 * @return
	 */
	private static String readAppNameFromYaml(ClassPathResource resource) {
		Yaml yaml = new Yaml();
		String appName = null;
		try (InputStream yamlInputStream = resource.getInputStream()) {
			Map<String, Object> yamlJson = yaml.load(yamlInputStream);
			appName = getYamlValue(APP_NAME_KEY, yamlJson);
		} catch (IOException e) {
			// 抛异常，则取当前jar名
			e.printStackTrace();
		}
		return appName;
	}
	
	@Override
	public String lookup(String key) {
		return DATA.get(key);
	}

	@Override
	public String lookup(LogEvent event, String key) {
		return DATA.get(key);
	}

	/**
	 * 解析yaml文件中的json
	 * 
	 * @param name
	 * @param yamlJson
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String getYamlValue(String name, Map<String, Object> yamlJson) {
		String[] keys = name.split(SPRING_YAML_KEY_SEPARATOR);
		if (Objects.isNull(keys) || keys.length == 0) {
			return null;
		}

		if (keys.length == 1) {
			return String.valueOf(yamlJson.get(keys[0]));
		}

		Map<String, Object> tempMap = null;
		String value = null;
		for (int i = 0; i < keys.length; i++) {
			if (i == 0) {
				Object tmp = yamlJson.get(keys[i]);
				if (tmp == null) {
					return null;
				}
				tempMap = (Map<String, Object>) tmp;
			} else if (i < keys.length - 1) {
				Object tmp = tempMap.get(keys[i]);
				if (tmp == null) {
					return null;
				}
				tempMap = (Map<String, Object>) tmp;
			} else {
				value = String.valueOf(tempMap.get(keys[i]));
			}
		}

		return value;
	}

}