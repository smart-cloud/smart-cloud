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
package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import feign.codec.Decoder;
import feign.codec.Encoder;
import org.apache.commons.collections4.MapUtils;
import org.smartframework.cloud.starter.rpc.feign.converter.ProtostuffHttpMessageConverter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * feign消息转换配置
 *
 * @author collin
 * @date 2019/12/2
 */
@Configuration
public class FeignMessageConverterAutoConfiguration {

    @Bean
    public ProtostuffHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtostuffHttpMessageConverter();
    }

    @Bean
    public Encoder springEncoder(final ApplicationContext context) {
        return new SpringEncoder(getMessageConverters(context));
    }

    @Bean
    public Decoder springDecoder(final ApplicationContext context) {
        return new ResponseEntityDecoder(new SpringDecoder(getMessageConverters(context)));
    }

    private ObjectFactory<HttpMessageConverters> getMessageConverters(ApplicationContext context) {
        return () -> MapUtils.isEmpty(context.getBeansOfType(HttpMessageConverters.class)) ?
                new HttpMessageConverters(new MappingJackson2HttpMessageConverter()) : context.getBean(HttpMessageConverters.class);
    }

}