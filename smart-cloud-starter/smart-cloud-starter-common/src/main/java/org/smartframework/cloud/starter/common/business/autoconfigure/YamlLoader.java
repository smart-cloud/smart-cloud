package org.smartframework.cloud.starter.common.business.autoconfigure;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.smartframework.cloud.starter.common.support.annotation.YamlScan;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @desc yaml文件加载
 * @author liyulin
 * @date 2020/02/23
 */
public class YamlLoader {
	
	/**
	 * 将启动类注解上配置的yaml文件加到environment中
	 * 
	 * @param environment
	 * @param mainApplicationClass
	 */
	public void loadYaml(ConfigurableEnvironment environment, Class<?> mainApplicationClass) {
		String[] locationPatterns = getLocationPatternsOnSpringBoot(mainApplicationClass);
		if (ArrayUtils.isEmpty(locationPatterns)) {
			return;
		}

		loadYaml(locationPatterns, environment);
	}

	/**
	 * case：application正常启动
	 * 
	 * @param mainApplicationClass
	 * @return
	 */
	private String[] getLocationPatternsOnSpringBoot(Class<?> mainApplicationClass) {
		YamlScan yamlScan = AnnotationUtils.findAnnotation(mainApplicationClass, YamlScan.class);
		if (Objects.isNull(yamlScan)) {
			return new String[0];
		}
		
		return yamlScan.locationPatterns();
	}

	/**
	 * 将匹配的yaml文件加到environment中
	 * <p>
	 * <b>NOTE</b>：此时日志配置还没有加载，还打不了日志
	 * 
	 * @param locationPatterns
	 * @param environment
	 */
	private void loadYaml(String[] locationPatterns, ConfigurableEnvironment environment) {
		// 1、获取每个文件对应的Resource对象
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Set<Resource> resourceSet = new HashSet<>();
		for (String locationPattern : locationPatterns) {
			try {
				Resource[] resources = resourcePatternResolver.getResources(locationPattern);
				if (ArrayUtils.isNotEmpty(resources)) {
					Collections.addAll(resourceSet, resources);
				}
			} catch (IOException e) {
				// 此处如果出现异常，用log打印将会失效。
				e.printStackTrace();
			}
		}

		if (resourceSet.isEmpty()) {
			return;
		}

		// 2、将所有Resource加入Environment中
		try {
			for (Resource resource : resourceSet) {
				System.out.println("load yaml ==> " + resource.getFilename());

				YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
				List<PropertySource<?>> propertySources = yamlPropertySourceLoader.load(resource.getFilename(),
						resource);
				for (PropertySource<?> propertySource : propertySources) {
					environment.getPropertySources().addLast(propertySource);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}