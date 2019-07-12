package org.smartframework.cloud.starter.mybatis.autoconfigure;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.smartframework.cloud.starter.log.util.LogUtil;
import org.smartframework.cloud.utility.CollectionUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.ConcurrentReferenceHashMap;

/**
 * 初始化<code>@Transactional</code>的属性value值
 *
 * @author liyulin
 * @date 2019年5月19日下午4:36:04
 */
public class InitTransactionalValue implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	private BeanFactory beanFactory;
	/** AbstractFallbackTransactionAttributeSource的属性attributeCache */
	private static final String ABSTRACTFALLBACKTRANSACTIONATTRIBUTE_ATTRIBUTECACHE = "attributeCache";
	/** MethodClassKey的属性method */
	private static final String METHODCLASSKEY_METHOD = "method";

	/** <带有事务注解类的包名, 事务名> */
	private static ConcurrentMap<String, String> multipleTransactionManagerInfoCache = new ConcurrentReferenceHashMap<>(
			4);

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		AbstractFallbackTransactionAttributeSource transactionAttributeSource = beanFactory
				.getBean(AbstractFallbackTransactionAttributeSource.class);
		Map<Object, TransactionAttribute> attributeCache = getField(transactionAttributeSource,
				AbstractFallbackTransactionAttributeSource.class, ABSTRACTFALLBACKTRANSACTIONATTRIBUTE_ATTRIBUTECACHE);
		if (CollectionUtil.isEmpty(attributeCache)) {
			return;
		}
		
		for (Map.Entry<Object, TransactionAttribute> entry : attributeCache.entrySet()) {
			if (StringUtils.isNotBlank(entry.getValue().getQualifier())
					|| !(entry.getValue() instanceof DefaultTransactionAttribute)) {
				continue;
			}

			DefaultTransactionAttribute transactionAttribute = (DefaultTransactionAttribute) entry.getValue();
			Method method = getField(entry.getKey(), MethodClassKey.class, METHODCLASSKEY_METHOD);
			if (method == null) {
				continue;
			}

			boolean isTransactionalMethod = (AnnotationUtils.findAnnotation(method, Transactional.class) != null);
			if (isTransactionalMethod) {
				String packageName = method.getDeclaringClass().getPackage().getName();
				String transactionManagerName = getTransactionManagerName(packageName);
				transactionAttribute.setQualifier(transactionManagerName);
			}
		}
	}

	public static ConcurrentMap<String, String> getMultipleTransactionManagerInfoCache() {
		return multipleTransactionManagerInfoCache;
	}

	/**
	 * 根据包名，获取事务名称
	 * 
	 * @param packageName
	 * @return
	 */
	private static String getTransactionManagerName(String packageName) {
		for (Map.Entry<String, String> entry : multipleTransactionManagerInfoCache.entrySet()) {
			if (packageName.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return "";
	}

	/**
	 * 通过反射获取对象中的属性值
	 * 
	 * @param object    实例对象
	 * @param clazz
	 * @param fieldName 属性名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getField(Object object, Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(object);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			LogUtil.error(e.getMessage(), e);
		}

		return null;
	}

}