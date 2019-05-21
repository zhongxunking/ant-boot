/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-16 23:11 创建
 */
package org.antframework.boot.env.refresh.placeholder;

import lombok.extern.slf4j.Slf4j;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.ConfigListener;
import org.antframework.boot.env.listener.support.DefaultConfigListener;
import org.antframework.boot.env.refresh.placeholder.injector.FieldPlaceholderInjector;
import org.antframework.boot.env.refresh.placeholder.injector.MethodPlaceholderInjector;
import org.antframework.boot.env.refresh.placeholder.injector.PlaceholderInjector;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 占位符刷新器
 */
@Order(PlaceholdersRefresher.ORDER)
@Slf4j
public class PlaceholdersRefresher implements BeanFactoryAware, BeanPostProcessor, ConfigListener {
    /**
     * 优先级
     */
    public static final int ORDER = DefaultConfigListener.ORDER - 20;

    // 配置key及对应的占位符注射器
    private final Map<String, Set<PlaceholderInjector>> keyInjectors = new ConcurrentHashMap<>();
    // bean工厂
    private ConfigurableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = AopUtils.getTargetClass(bean);
        // 解析@Value字段
        ReflectionUtils.doWithFields(beanClass, field -> {
            if (Modifier.isStatic(field.getModifiers())) {
                return;
            }
            Value valueAnnotation = AnnotatedElementUtils.findMergedAnnotation(field, Value.class);
            if (valueAnnotation == null) {
                return;
            }
            ReflectionUtils.makeAccessible(field);
            addInjector(new FieldPlaceholderInjector(valueAnnotation.value(), bean, field));
        });
        // 解析@Value方法
        ReflectionUtils.doWithMethods(beanClass, method -> {
            if (Modifier.isStatic(method.getModifiers()) || method.getParameterCount() != 1) {
                return;
            }
            Value valueAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, Value.class);
            if (valueAnnotation == null) {
                return;
            }
            ReflectionUtils.makeAccessible(method);
            addInjector(new MethodPlaceholderInjector(valueAnnotation.value(), bean, method));
        });
        return bean;
    }

    // 添加注射器
    private void addInjector(PlaceholderInjector injector) {
        Set<String> keys = PlaceholderParser.parse(injector.getPlaceholder());
        for (String key : keys) {
            Set<PlaceholderInjector> injectors = keyInjectors.computeIfAbsent(key, k -> new HashSet<>());
            injectors.add(injector);
        }
    }

    @Override
    public void onChange(String appId, List<ChangedProperty> changedProperties) {
        if (!Objects.equals(appId, Contexts.getAppId())) {
            return;
        }
        log.info("刷新@Value占位符");
        changedProperties.stream()
                .map(ChangedProperty::getKey)
                .map(keyInjectors::get)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .distinct()
                .forEach(injector -> injector.inject(beanFactory));
    }

    // 兼容SpringBoot1.x
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
