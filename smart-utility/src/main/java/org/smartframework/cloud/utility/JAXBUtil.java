package org.smartframework.cloud.utility;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.experimental.UtilityClass;

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
 * @author liyulin
 * @date 2019-04-17
 */
@UtilityClass
public final class JAXBUtil {

	/** cache JAXBContext */
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
		JAXBContext context = getJAXBContext(bean.getClass());

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
	 * @param xml xml文本
	 * @param beanClass
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml, Class<T> beanClass) throws JAXBException, IOException {
		JAXBContext context = getJAXBContext(beanClass);

		Unmarshaller unmarshaller = context.createUnmarshaller();
		Object bean = unmarshaller.unmarshal(new StringReader(xml));
		return (T) bean;
	}

	private static JAXBContext getJAXBContext(Class<?> beanClass) throws JAXBException {
		JAXBContext context = jaxbContextCache.getIfPresent(beanClass);
		if (Objects.isNull(context)) {
			context = JAXBContext.newInstance(beanClass);
			jaxbContextCache.put(beanClass, context);
		}

		return context;
	}

}