package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import org.smartframework.cloud.starter.rpc.feign.converter.ProtostuffHttpMessageConverter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * @author liyulin
 * @desc feign消息转换配置
 * @date 2019/12/2
 */
@Configuration
public class FeignMessageConverterAutoConfigure {


    @Bean
    public ProtostuffHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtostuffHttpMessageConverter();
    }

    @Bean
    public Encoder springEncoder(final ObjectFactory<HttpMessageConverters> messageConverterObjectFactory) {
        return new SpringEncoder(messageConverterObjectFactory);
    }

    @Bean
    public Decoder springDecoder(final ObjectFactory<HttpMessageConverters> messageConverterObjectFactory) {
        return new ResponseEntityDecoder(new SpringDecoder(messageConverterObjectFactory));
    }

}