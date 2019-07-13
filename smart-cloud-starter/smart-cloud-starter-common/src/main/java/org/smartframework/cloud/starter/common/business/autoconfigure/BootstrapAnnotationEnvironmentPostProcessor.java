package org.smartframework.cloud.starter.common.business.autoconfigure;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.smartframework.cloud.starter.common.business.util.AnnotatedClassFinder;
import org.smartframework.cloud.starter.common.constants.PackageConfig;
import org.smartframework.cloud.starter.common.support.annotation.SmartSpringCloudApplication;
import org.smartframework.cloud.starter.common.support.annotation.YamlScan;
import org.smartframework.cloud.utility.ArrayUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 启动类注解值读取
 * 
 * @author liyulin
 * @date 2019-06-21
 */
public class BootstrapAnnotationEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (isRegisterShutdownHook(application)) {
			Class<?> mainApplicationClass = application.getMainApplicationClass();
			// 1、设置{@link ComponentScan}的{@code basePackages}
			SmartSpringCloudApplication smartSpringCloudApplication = AnnotationUtils
					.findAnnotation(mainApplicationClass, SmartSpringCloudApplication.class);
			if (smartSpringCloudApplication == null) {
				mainApplicationClass = new AnnotatedClassFinder(SmartSpringCloudApplication.class)
						.findFromClass(mainApplicationClass);// 此处findFromClass的参数为测试启动类
				if (mainApplicationClass == null) {
					return;
				}
				smartSpringCloudApplication = AnnotationUtils
						.findAnnotation(mainApplicationClass, SmartSpringCloudApplication.class);
			}
			if (smartSpringCloudApplication == null) {
				return;
			}
			String[] componentBasePackages = smartSpringCloudApplication.componentBasePackages();
			PackageConfig.setBasePackages(componentBasePackages);

			// 2、加载yml
			loadYaml(environment, mainApplicationClass);
		}
	}

	private boolean isRegisterShutdownHook(SpringApplication application) {
		boolean registerShutdownHook = false;
		try {
			Field field = SpringApplication.class.getDeclaredField("registerShutdownHook");
			field.setAccessible(true);
			registerShutdownHook = field.getBoolean(application);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return registerShutdownHook;
	}

	/**
	 * 将启动类注解上配置的yaml文件加到environment中
	 * 
	 * @param environment
	 * @param mainApplicationClass
	 */
	private void loadYaml(ConfigurableEnvironment environment, Class<?> mainApplicationClass) {
		String[] locationPatterns = getLocationPatternsOnSpringBoot(mainApplicationClass);
		if (ArrayUtil.isEmpty(locationPatterns)) {
			return;
		}

		loadYaml(locationPatterns, environment);
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
				if (resources != null && resources.length > 0) {
					for (Resource resource : resources) {
						resourceSet.add(resource);
					}
				}
			} catch (IOException e) {
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

}