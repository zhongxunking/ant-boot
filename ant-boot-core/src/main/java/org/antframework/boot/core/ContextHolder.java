/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:40 创建
 */
package org.antframework.boot.core;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * 上下文持有器
 */
public class ContextHolder {
    // 上下文
    private static Context context;

    /**
     * 初始化上下文
     *
     * @param applicationContext spring应用上下文
     */
    public static void initContext(ApplicationContext applicationContext) {
        if (context != null) {
            throw new IllegalStateException("Context已经初始化，不能重复初始化");
        }
        context = new Context();
        context.applicationContext = applicationContext;
    }

    /**
     * 获取ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context.applicationContext;
    }

    /**
     * 获取Environment
     */
    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 获取你自动扫描基础包
     */
    public static String[] getBasePackages() {
        List<String> basePackages = AutoConfigurationPackages.get(getApplicationContext());
        return basePackages.toArray(new String[basePackages.size()]);
    }

    // 上下文
    private static class Context {
        private ApplicationContext applicationContext;
    }
}
