/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:40 创建
 */
package org.antframework.boot.core;

import org.antframework.boot.core.util.PropertiesBinder;
import org.antframework.boot.core.util.PropertiesBinderV1;
import org.antframework.common.util.other.PropertyUtils;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 上下文持有器
 */
public final class Contexts {
    /**
     * 应用id的key
     */
    public static final String APP_ID_KEY = "spring.application.name";
    /**
     * 应用home目录的key
     */
    public static final String HOME_KEY = "app.home";
    // spring环境
    private static ConfigurableEnvironment environment = null;
    // spring容器
    private static ConfigurableApplicationContext applicationContext = null;

    /**
     * 设置spring环境
     */
    public static void setEnvironment(ConfigurableEnvironment environment) {
        Contexts.environment = environment;
    }

    /**
     * 设置spring容器
     */
    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        Contexts.applicationContext = applicationContext;
    }

    /**
     * 获取应用的home目录
     */
    public static String getHome() {
        String home = getProperty(HOME_KEY);
        if (home == null) {
            home = "/var/apps/" + getAppId();
        }
        return home;
    }

    /**
     * 获取应用id（如果未配置应用Id，则返回null）
     */
    public static String getAppId() {
        return getProperty(APP_ID_KEY);
    }

    /**
     * 获取配置value
     *
     * @param key 配置key
     * @return 配置value
     */
    public static String getProperty(String key) {
        if (getEnvironment() == null) {
            return PropertyUtils.getProperty(key);
        } else {
            return getEnvironment().getProperty(key);
        }
    }

    /**
     * 获取Environment
     */
    public static Environment getEnvironment() {
        if (getApplicationContext() == null) {
            return environment;
        } else {
            return getApplicationContext().getEnvironment();
        }
    }

    /**
     * 获取ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取自动扫描的包路径
     */
    public static String[] getBasePackages() {
        Assert.notNull(getApplicationContext(), "过早的获取自动扫描的包路径");
        List<String> basePackages = AutoConfigurationPackages.get(getApplicationContext());
        return basePackages.toArray(new String[basePackages.size()]);
    }

    /**
     * 构建配置对象
     *
     * @param targetClass 目标类型
     * @return 属性对象
     */
    public static <T> T buildProperties(Class<T> targetClass) {
        Assert.notNull(getEnvironment(), "过早的构建配置对象");
        if (ClassUtils.isPresent("org.springframework.boot.context.properties.bind.Binder", Contexts.class.getClassLoader())) {
            PropertiesBinder binder = new PropertiesBinder(((ConfigurableEnvironment) getEnvironment()).getPropertySources());
            return binder.build(targetClass);
        } else {
            // 兼容SpringBoot1.x
            PropertiesBinderV1 binderV1 = new PropertiesBinderV1(((ConfigurableEnvironment) getEnvironment()).getPropertySources());
            return binderV1.build(targetClass);
        }
    }
}
