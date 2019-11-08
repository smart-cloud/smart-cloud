package org.smartframework.cloud.code.generate.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.smartframework.cloud.code.generate.config.Config;
import org.smartframework.cloud.code.generate.properties.YamlProperties;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import lombok.extern.slf4j.Slf4j;

/**
 * yaml读取工具类
 * 
 * @author liyulin
 * @date 2019-07-15
 */
@Slf4j
public final class YamlUtil {

	private YamlUtil() {
	}

	/**
	 * 从yaml文件去读配置信息
	 * 
	 * @return
	 */
	public static YamlProperties readYamlProperties() {
		// 获取yaml文件路径
		ClassPathResource resource = new ClassPathResource(getYamlPath());
		// 创建Yaml对象
		Representer representer = new Representer();
		representer.getPropertyUtils().setSkipMissingProperties(true);
		Yaml yaml = new Yaml(representer);
		// 读取yaml文件信息
		YamlProperties yamlProperties = null;
		try (InputStream yamlInputStream = resource.getInputStream()) {
			yamlProperties = yaml.loadAs(yamlInputStream, YamlProperties.class);
		} catch (IOException e) {
			log.error("load yaml error", e);
		}
		log.info("yamlProperties==>{}", yamlProperties);

		return yamlProperties;
	}

	/**
	 * 从properties文件获取配置的yaml文件路径
	 * 
	 * @return
	 */
	private static String getYamlPath() {
		ResourceBundle configResource = ResourceBundle.getBundle(Config.CONFIG_NAME);
		String configPath = configResource.getString(Config.PROPERTIES_KEY);
		if (!configPath.endsWith(YAML_SUFFIX)) {
			configPath = configPath + YAML_SUFFIX;
		}
		return Config.CONFIG_PATH + configPath;
	}

	/** yaml文件后缀 */
	private static final String YAML_SUFFIX = ".yaml";

}