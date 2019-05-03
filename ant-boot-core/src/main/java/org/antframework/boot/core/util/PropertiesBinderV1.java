/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-03 18:18 创建
 */
package org.antframework.boot.core.util;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.reflect.Method;

/**
 * 属性绑定器（兼容SpringBoot1.x）
 */
@AllArgsConstructor
public class PropertiesBinderV1 {
    // 转换器
    private static final ConversionService CONVERSION_SERVICE = new DefaultConversionService();
    // 校验器
    private static final Validator VALIDATOR;
    // PropertiesConfigurationFactory反射调用相关的资源
    private static final Class FACTORY_CLASS;
    private static final Method SET_PROPERTY_SOURCES;
    private static final Method SET_VALIDATOR;
    private static final Method SET_CONVERSION_SERVICE;
    private static final Method SET_TARGET_NAME;
    private static final Method SET_IGNORE_INVALID_FIELDS;
    private static final Method SET_IGNORE_NESTED_PROPERTIES;
    private static final Method SET_IGNORE_UNKNOWN_FIELDS;
    private static final Method BIND_PROPERTIES_TO_TARGET;

    static {
        // 初始化校验器
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        VALIDATOR = validator;
        // 初始化PropertiesConfigurationFactory反射调用相关的资源
        Class factoryClass;
        try {
            factoryClass = Class.forName("org.springframework.boot.bind.PropertiesConfigurationFactory");
        } catch (ClassNotFoundException e) {
            factoryClass = ExceptionUtils.rethrow(e);
        }
        FACTORY_CLASS = factoryClass;
        SET_PROPERTY_SOURCES = ReflectionUtils.findMethod(FACTORY_CLASS, "setPropertySources", PropertySources.class);
        ReflectionUtils.makeAccessible(SET_PROPERTY_SOURCES);
        SET_VALIDATOR = ReflectionUtils.findMethod(FACTORY_CLASS, "setValidator", Validator.class);
        ReflectionUtils.makeAccessible(SET_VALIDATOR);
        SET_CONVERSION_SERVICE = ReflectionUtils.findMethod(FACTORY_CLASS, "setConversionService", ConversionService.class);
        ReflectionUtils.makeAccessible(SET_CONVERSION_SERVICE);
        SET_TARGET_NAME = ReflectionUtils.findMethod(FACTORY_CLASS, "setTargetName", String.class);
        ReflectionUtils.makeAccessible(SET_TARGET_NAME);
        SET_IGNORE_INVALID_FIELDS = ReflectionUtils.findMethod(FACTORY_CLASS, "setIgnoreInvalidFields", boolean.class);
        ReflectionUtils.makeAccessible(SET_IGNORE_INVALID_FIELDS);
        SET_IGNORE_NESTED_PROPERTIES = ReflectionUtils.findMethod(FACTORY_CLASS, "setIgnoreNestedProperties", boolean.class);
        ReflectionUtils.makeAccessible(SET_IGNORE_NESTED_PROPERTIES);
        SET_IGNORE_UNKNOWN_FIELDS = ReflectionUtils.findMethod(FACTORY_CLASS, "setIgnoreUnknownFields", boolean.class);
        ReflectionUtils.makeAccessible(SET_IGNORE_UNKNOWN_FIELDS);
        BIND_PROPERTIES_TO_TARGET = ReflectionUtils.findMethod(FACTORY_CLASS, "bindPropertiesToTarget");
        ReflectionUtils.makeAccessible(BIND_PROPERTIES_TO_TARGET);
    }

    // 属性资源
    private final PropertySources propertySources;

    /**
     * 构建属性对象
     *
     * @param targetClass 目标类型（必须有默认构造函数。如果被@ConfigurationProperties标注，则以注解为准进行属性绑定；否则按照属性前缀为null进行处理）
     * @return 绑定属性后的对象
     */
    public <T> T build(Class<T> targetClass) {
        T target = (T) ReflectUtils.newInstance(targetClass);
        ConfigurationProperties annotation = AnnotatedElementUtils.findMergedAnnotation(targetClass, ConfigurationProperties.class);
        if (annotation != null) {
            bind(target, annotation.prefix(), annotation.ignoreInvalidFields(), false, annotation.ignoreUnknownFields());
        } else {
            bind(target, null);
        }
        return target;
    }

    /**
     * 绑定属性
     *
     * @param target 目标对象
     * @param prefix 属性前缀
     */
    public void bind(Object target, String prefix) {
        bind(target, prefix, false, false, true);
    }

    /**
     * 绑定属性
     *
     * @param target                 目标对象
     * @param prefix                 属性前缀
     * @param ignoreInvalidFields    是否忽略类型不匹配的字段
     * @param ignoreNestedProperties 是否忽略内嵌型的属性
     * @param ignoreUnknownFields    是否忽略未知字段
     */
    public void bind(Object target, String prefix, boolean ignoreInvalidFields, boolean ignoreNestedProperties, boolean ignoreUnknownFields) {
        Object factory = ReflectUtils.newInstance(FACTORY_CLASS, new Class[]{Object.class}, new Object[]{target});
        ReflectionUtils.invokeMethod(SET_PROPERTY_SOURCES, factory, propertySources);
        ReflectionUtils.invokeMethod(SET_VALIDATOR, factory, VALIDATOR);
        ReflectionUtils.invokeMethod(SET_CONVERSION_SERVICE, factory, CONVERSION_SERVICE);
        ReflectionUtils.invokeMethod(SET_TARGET_NAME, factory, prefix);
        ReflectionUtils.invokeMethod(SET_IGNORE_INVALID_FIELDS, factory, ignoreInvalidFields);
        ReflectionUtils.invokeMethod(SET_IGNORE_NESTED_PROPERTIES, factory, ignoreNestedProperties);
        ReflectionUtils.invokeMethod(SET_IGNORE_UNKNOWN_FIELDS, factory, ignoreUnknownFields);
        ReflectionUtils.invokeMethod(BIND_PROPERTIES_TO_TARGET, factory);
    }
}
