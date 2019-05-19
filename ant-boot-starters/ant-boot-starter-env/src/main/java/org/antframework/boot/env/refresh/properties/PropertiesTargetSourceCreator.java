/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-19 18:26 创建
 */
package org.antframework.boot.env.refresh.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.ConfigListener;
import org.antframework.boot.env.listener.support.DefaultConfigListener;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.TargetSourceCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置TargetSource创建器
 */
@Order(PropertiesTargetSourceCreator.ORDER)
@Slf4j
public class PropertiesTargetSourceCreator implements TargetSourceCreator, ConfigListener {
    /**
     * 优先级
     */
    public static final int ORDER = DefaultConfigListener.ORDER - 10;

    // key前缀与对应的配置TargetSource
    private final Map<String, Set<PropertiesTargetSource>> prefixTargetSources = new ConcurrentHashMap<>();
    // 配置
    private final RefreshProperties properties = Contexts.buildProperties(RefreshProperties.class);
    // 需忽略的bean
    private Set<String> ignoredBeanNames = new HashSet<>();

    /**
     * 设置需忽略的bean
     *
     * @param ignoredBeanNames 需忽略的bean
     */
    public void setIgnoredBeanNames(Set<String> ignoredBeanNames) {
        this.ignoredBeanNames = ignoredBeanNames;
    }

    @Override
    public TargetSource getTargetSource(Class<?> beanClass, String beanName) {
        ConfigurationProperties propertiesAnnotation = AnnotatedElementUtils.findMergedAnnotation(beanClass, ConfigurationProperties.class);
        Refreshable refreshableAnnotation = AnnotatedElementUtils.findMergedAnnotation(beanClass, Refreshable.class);
        if (ignoredBeanNames.contains(beanName)
                || propertiesAnnotation == null
                || (refreshableAnnotation == null && !properties.getRefreshableClasses().contains(beanClass.getName()))) {
            return null;
        }
        PropertiesTargetSource targetSource = new PropertiesTargetSource(beanClass);
        prefixTargetSources.compute(propertiesAnnotation.prefix(), (prefix, targetSources) -> {
            if (targetSources == null) {
                targetSources = new HashSet<>();
            }
            targetSources.add(targetSource);
            return targetSources;
        });
        return targetSource;
    }

    @Override
    public void onChange(String appId, List<ChangedProperty> changedProperties) {
        if (!Objects.equals(appId, Contexts.getAppId())) {
            return;
        }
        log.info("刷新@ConfigurationProperties配置");
        changedProperties.stream()
                .map(ChangedProperty::getKey)
                .map(this::getMatchedTargetSources)
                .flatMap(Set::stream)
                .distinct()
                .forEach(PropertiesTargetSource::refresh);
    }

    // 获取匹配的TargetSource
    private Set<PropertiesTargetSource> getMatchedTargetSources(String key) {
        Set<PropertiesTargetSource> matchedTargetSources = new HashSet<>();
        prefixTargetSources.forEach((prefix, targetSources) -> {
            if (key.startsWith(prefix)) {
                matchedTargetSources.addAll(targetSources);
            }
        });
        return matchedTargetSources;
    }

    /**
     * 刷新配置
     */
    @ConfigurationProperties("ant.env.refresh-properties")
    @Getter
    @Setter
    public static class RefreshProperties {
        /**
         * 可刷新的类型
         */
        @NotNull
        private Set<String> refreshableClasses = new HashSet<>();
    }
}
