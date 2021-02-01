package org.smartframework.cloud.starter.core.test.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smartframework.cloud.starter.core.business.util.ReflectionUtil;
import org.smartframework.cloud.starter.core.constants.PackageConfig;

import java.util.Set;

public class ReflectionUtilUnitTest {

    @Test
    public void testGetSubTypesOf() {
        PackageConfig.setBasePackages(new String[]{Animal.class.getPackage().getName()});
        Set<Class<? extends Animal>> set = ReflectionUtil.getSubTypesOf(Animal.class);
        Assertions.assertThat(set).isNotEmpty();
    }

    class Animal {
    }

    class Dog extends Animal {
    }

}