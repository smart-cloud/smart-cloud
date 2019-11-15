package org.smartframework.cloud.mask.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.smartframework.cloud.mask.EnableMask;
import org.smartframework.cloud.mask.MaskRule;
import org.smartframework.cloud.mask.serialize.MaskSerializeConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * @author liyulin
 * @desc mask工具类
 * @date 2019-11-05
 */
@Slf4j
public final class MaskUtil {

    private static final Cache<Class<?>, Boolean> MASK_CLASS_CACHE = CacheBuilder.newBuilder()
            // 设置并发级别
            .concurrencyLevel(Runtime.getRuntime().availableProcessors() << 1)
            // 设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(1024 * 1024 * 16)
            // 使用弱引用
            .weakValues().build();

    private MaskUtil() {
    }

    /**
     * 根据{@link MaskRule}进行截取替换
     *
     * @param s
     * @param maskRule
     * @return
     */
    public static String mask(String s, MaskRule maskRule) {
        return mask(s, maskRule.getStartLen(), maskRule.getEndLen(), maskRule.getMask());
    }

    /**
     * 根据传入的mask规则，替换字符串
     *
     * @param obj
     * @param start
     * @param end
     * @param mask
     * @return
     */
    public static String mask(Object obj, int start, int end, String mask) {
        if (obj == null) {
            return mask;
        }
        return mask(obj.toString(), start, end, mask);
    }

    /**
     * 根据传入的mask规则，替换字符串
     *
     * @param s
     * @param start
     * @param end
     * @param mask
     * @return
     */
    public static String mask(String s, int start, int end, String mask) {
        if (s == null || s.length() == 0) {
            return mask;
        }
        int len = s.length();
        if (len <= start) {
            return mask;
        }
        if (len <= start + end) {
            return s.substring(0, start) + mask;
        }

        return s.substring(0, start) + mask + s.substring(len - end);
    }

    /**
     * 对敏感数据进行脱敏
     *
     * @param object
     * @return
     */
    public static String mask(Object object) {
        // 注意：使用SerializerFeature时，不能禁用“循环引用检测”
        return JSON.toJSONString(object, MaskSerializeConfig.GLOBAL_INSTANCE);
    }

    /**
     * 将对象中需要打掩码的单独打掩码
     *
     * @param t
     * @return
     */
    public static <T> T wrapMask(T t) {
        if (t == null) {
            return null;
        }

        Class<?> clazz = t.getClass();
        boolean needMask = isNeedMask(clazz);
        if (!needMask) {
            return (T) t;
        }

        String mask = MaskUtil.mask(t);
        TypeVariable<? extends Class<?>>[] typeVariables = clazz.getTypeParameters();
        // 非泛型对象
        if (typeVariables == null || typeVariables.length == 0) {
            return (T) JSON.parseObject(mask, clazz);
        }

        // 泛型对象
        Field[] fields = clazz.getDeclaredFields();
        Type[] actualTypeArguments = new Type[typeVariables.length];
        for (int i = 0; i < typeVariables.length; i++) {
            TypeVariable<? extends Class<?>> typeVariable = typeVariables[i];
            for (Field field : fields) {
                if (Objects.equals(field.getGenericType().getTypeName(), typeVariable.getName())) {
					try {
						field.setAccessible(true);
						Object fieldValue = field.get(t);
						actualTypeArguments[i] = fieldValue.getClass();
					} catch (IllegalAccessException e) {
						log.error("get field error", e);
					}
					break;
                }
            }
        }
        Type type = t.getClass().getComponentType();
        return JSON.parseObject(mask, new TypeReference<T>(actualTypeArguments) {
        });
//		return JSON.parseObject(mask, new TypeReference<T>() {
//		});
    }

    /**
     * 是否需要mask
     *
     * @param clazz
     * @return
     */
    public static boolean isNeedMask(Class<?> clazz) {
        Boolean needMask = MASK_CLASS_CACHE.getIfPresent(clazz);
        if (needMask != null) {
            return needMask;
        }

        needMask = clazz.isAnnotationPresent(EnableMask.class);
        MASK_CLASS_CACHE.put(clazz, needMask);
        return needMask;
    }

}