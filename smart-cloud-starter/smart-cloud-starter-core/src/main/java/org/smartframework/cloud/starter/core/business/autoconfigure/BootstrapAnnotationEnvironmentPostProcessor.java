package org.smartframework.cloud.starter.core.business.autoconfigure;

import java.lang.reflect.Field;

import org.smartframework.cloud.starter.core.business.util.AnnotatedClassFinder;
import org.smartframework.cloud.starter.core.support.annotation.SmartSpringCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;

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
			// 1、获取启动类、启动类注解
			SmartSpringCloudApplication smartSpringCloudApplication = AnnotationUtils
					.findAnnotation(mainApplicationClass, SmartSpringCloudApplication.class);
			if (smartSpringCloudApplication == null) {
				mainApplicationClass = new AnnotatedClassFinder(SmartSpringCloudApplication.class)
						.findFromClass(mainApplicationClass);// 此处findFromClass的参数为测试启动类
				if (mainApplicationClass == null) {
					return;
				}
				smartSpringCloudApplication = AnnotationUtils.findAnnotation(mainApplicationClass,
						SmartSpringCloudApplication.class);
			}
			if (smartSpringCloudApplication == null) {
				return;
			}

			// 2、设置{@link ComponentScan}的{@code basePackages}
			new BasePackagesInitializer().init(smartSpringCloudApplication);

			// 3、加载yml
			new YamlLoader().loadYaml(environment, mainApplicationClass);
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

}