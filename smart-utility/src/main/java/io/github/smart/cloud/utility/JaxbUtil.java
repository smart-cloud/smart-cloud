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
package io.github.smart.cloud.utility;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * JAXB相关工具类
 *
 * <h2>功能</h2>
 *
 * <ul>
 * <li>bean转xml</li>
 * <li>xml转bean</li>
 * </ul>
 *
 * @author collin
 * @date 2019-04-17
 */
public final class JaxbUtil {

    /**
     * cache JAXBContext
     */
    private static Cache<Class<?>, JAXBContext> jaxbContextCache = CacheBuilder.newBuilder()
            // 设置并发级别
            .concurrencyLevel(Runtime.getRuntime().availableProcessors() << 1)
            // 设置写缓存30天后过期
            .expireAfterWrite(30, TimeUnit.DAYS)
            // 设置缓存最大容量为512，超过512之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(512)
            // 使用弱引用
            .weakValues()
            .build();

    private JaxbUtil() {
    }

    /**
     * bean转xml
     *
     * @param bean
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object bean) throws JAXBException {
        return beanToXml(bean, StandardCharsets.UTF_8.name());
    }

    /**
     * bean转xml
     *
     * @param bean
     * @param charsetName
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object bean, String charsetName) throws JAXBException {
        JAXBContext context = getJaxbContext(bean.getClass());

        Marshaller marshaller = context.createMarshaller();
        // 格式化生成的xml
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, charsetName);

        StringWriter writer = new StringWriter();
        marshaller.marshal(bean, writer);
        return writer.toString();
    }

    /**
     * xml转bean
     *
     * @param xml       xml文本
     * @param beanClass
     * @return
     * @throws JAXBException
     * @throws IOException
     */
    public static <T> T xmlToBean(String xml, Class<T> beanClass) throws JAXBException {
        JAXBContext context = getJaxbContext(beanClass);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object bean = unmarshaller.unmarshal(new StringReader(xml));
        return (T) bean;
    }

    private static JAXBContext getJaxbContext(Class<?> beanClass) throws JAXBException {
        JAXBContext context = jaxbContextCache.getIfPresent(beanClass);
        if (Objects.isNull(context)) {
            context = JAXBContext.newInstance(beanClass);
            jaxbContextCache.put(beanClass, context);
        }

        return context;
    }

}