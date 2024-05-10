/*
 * Copyright © 2019 collin (1634753825@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.smart.cloud.starter.locale.autoconfigure;

import io.github.smart.cloud.starter.configure.SmartAutoConfiguration;
import io.github.smart.cloud.starter.configure.properties.LocaleProperties;
import io.github.smart.cloud.starter.configure.properties.SmartProperties;
import io.github.smart.cloud.starter.locale.aspect.LocaleInterceptor;
import io.github.smart.cloud.starter.locale.constant.LocaleConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * 多语言配置
 *
 * @author collin
 * @date 2019-07-15
 */
@Slf4j
@Configuration
@AutoConfigureAfter(SmartAutoConfiguration.class)
public class LocaleAutoConfiguration {

    @Bean
    public MessageSource messageSource(final SmartProperties smartProperties) {
        LocaleProperties localeProperties = smartProperties.getLocale();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources(LocaleConstant.LOCALE_PATTERN);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        if (resources != null) {
            String[] strResources = new String[resources.length];
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                strResources[i] = LocaleConstant.LOCALE_DIR
                        + resource.getFilename().replace(LocaleConstant.LOCALE_PROPERTIES_SUFFIX, "");
            }
            messageSource.setBasenames(strResources);
        }
        messageSource.setDefaultEncoding(localeProperties.getEncoding());
        messageSource.setCacheSeconds(localeProperties.getCacheSeconds());
        return messageSource;
    }

    @Bean
    public LocaleInterceptor localeInterceptor(final MessageSource messageSource) {
        return new LocaleInterceptor(messageSource);
    }

    @Bean
    public Advisor localeAdvisor(final LocaleInterceptor localeInterceptor) {
        StringBuilder expression = new StringBuilder(512);
        expression.append("(@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))") //NOPMD - suppressed ConsecutiveLiteralAppends - TODO explain reason for suppression
                .append("&&")
                .append('(')
                .append("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
                .append("|| @annotation(org.springframework.web.bind.annotation.GetMapping)")
                .append("|| @annotation(org.springframework.web.bind.annotation.PostMapping)")
                .append("|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
                .append("|| @annotation(org.springframework.web.bind.annotation.PutMapping)")
                .append("|| @annotation(org.springframework.web.bind.annotation.PatchMapping)")
                .append(')');

        AspectJExpressionPointcut localePointcut = new AspectJExpressionPointcut();
        localePointcut.setExpression(expression.toString());

        DefaultBeanFactoryPointcutAdvisor apiLogAdvisor = new DefaultBeanFactoryPointcutAdvisor();
        apiLogAdvisor.setAdvice(localeInterceptor);
        apiLogAdvisor.setPointcut(localePointcut);

        return apiLogAdvisor;
    }

}