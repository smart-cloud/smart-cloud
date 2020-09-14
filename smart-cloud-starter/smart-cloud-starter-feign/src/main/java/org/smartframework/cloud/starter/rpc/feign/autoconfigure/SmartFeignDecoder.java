package org.smartframework.cloud.starter.rpc.feign.autoconfigure;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.smartframework.cloud.utility.SerializingUtil;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class SmartFeignDecoder implements Decoder {

    @Override
    public Object decode(final Response response, Type type) throws IOException, FeignException {
        if (type instanceof Class || type instanceof ParameterizedType || type instanceof WildcardType) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> c = (Class<?>) parameterizedType.getRawType();
                return SerializingUtil.deserialize(response.body().asInputStream(), c);
            }
            return SerializingUtil.deserialize(response.body().asInputStream(), type.getClass());
        }
        throw new DecodeException(response.status(), "type is not an instance of Class or ParameterizedType: " + type,
                response.request());
    }

}