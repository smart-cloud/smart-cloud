package org.smartframework.cloud.starter.common.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动加载匹配的yaml文件
 *
 * <p>
 * <b>NOTE</b>：该注解必须作用在启动类上才会生效!!!
 * 
 * <p>
 * 此注解的解析不能通过<code>@Import</code>注解；否则，类似<code>@ConditionalOnProperty</code>这种条件注解将不会生效
 *
 * @author liyulin
 * @date 2019-05-11
 * @since YamlEnvironmentPostProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface YamlScan {

	/** 属性名locationPatterns */
	public static final String ATTRIBUTE_LOCATION_PATTERNS = "locationPatterns";

	/** yml文件路径（支持正则表达式） */
	String[] locationPatterns();

}