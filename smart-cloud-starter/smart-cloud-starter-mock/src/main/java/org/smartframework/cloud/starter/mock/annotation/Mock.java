package org.smartframework.cloud.starter.mock.annotation;

import java.lang.annotation.*;

/**
 * method mock
 *
 * @author collin
 * @date 2021-07-31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mock {
}