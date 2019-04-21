/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-21 22:19 创建
 */
package org.antframework.boot.core.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.PropertySources;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 属性绑定器
 */
public class PropertiesBinder {
    // 校验器
    private static final Validator VALIDATOR;

    static {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        VALIDATOR = validator;
    }

    // 绑定器
    private final Binder binder;

    public PropertiesBinder(PropertySources propertySources) {
        binder = new Binder(ConfigurationPropertySources.from(propertySources),
                new PropertySourcesPlaceholdersResolver(propertySources));
    }

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
            bind(target, annotation.prefix(), annotation.ignoreInvalidFields(), annotation.ignoreUnknownFields());
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
        bind(target, prefix, false, true);
    }

    /**
     * 绑定属性
     *
     * @param target              目标对象
     * @param prefix              属性前缀
     * @param ignoreInvalidFields 是否忽略无效的字段
     * @param ignoreUnknownFields 是否忽略未知字段
     */
    public void bind(Object target, String prefix, boolean ignoreInvalidFields, boolean ignoreUnknownFields) {
        Validated annotation = AnnotatedElementUtils.findMergedAnnotation(target.getClass(), Validated.class);
        Bindable bindable = Bindable.ofInstance(target);
        if (annotation != null) {
            bindable = bindable.withAnnotations(annotation);
        }
        BindHandler handler = buildBindHandler(ignoreInvalidFields, ignoreUnknownFields, annotation != null);
        binder.bind(prefix, bindable, handler);
    }

    // 构建绑定处理器
    private BindHandler buildBindHandler(boolean ignoreInvalidFields, boolean ignoreUnknownFields, boolean validate) {
        BindHandler handler = new IgnoreTopLevelConverterNotFoundBindHandler();
        if (ignoreInvalidFields) {
            handler = new IgnoreErrorsBindHandler(handler);
        }
        if (!ignoreUnknownFields) {
            UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
            handler = new NoUnboundElementsBindHandler(handler, filter);
        }
        if (validate) {
            handler = new ValidationBindHandler(handler, VALIDATOR);
        }
        return handler;
    }
}
