/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.core.boot;

import org.antframework.boot.core.AntBootApplication;
import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.common.util.file.FileUtils;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ant应用RunListener
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AntApplicationRunListener implements SpringApplicationRunListener {
    // 应用名属性名
    private static final String APPLICATION_NAME_PROPERTY_NAME = "spring.application.name";

    private SpringApplication springApplication;
    private ApplicationArguments arguments;

    public AntApplicationRunListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        this.arguments = new DefaultApplicationArguments(args);
        initApp(springApplication);
    }

    @Override
    public void starting() {
        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8)) {
            throw new IllegalStateException("请使用jdk1.8及以上版本");
        }
        if (Integer.parseInt(SpringVersion.getVersion().substring(0, 1)) < 4) {
            throw new IllegalStateException("请使用spring4.x版本");
        }
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        Contexts.setEnvironment(environment);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        Contexts.setApplicationContext(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }

    // 初始化App
    private void initApp(SpringApplication springApplication) {
        // 查找@AntBootApplication
        AntBootApplication annotation = null;
        for (Object source : springApplication.getSources()) {
            if (!(source instanceof Class)) {
                throw new IllegalArgumentException("source必须是Class");
            }
            annotation = AnnotatedElementUtils.findMergedAnnotation((Class) source, AntBootApplication.class);
            if (annotation != null) {
                break;
            }
        }
        if (annotation == null) {
            throw new IllegalArgumentException("sources中无@AntBootApplication注解");
        }
        // 初始化App
        Apps.initApp(annotation.appCode());
        // 创建配置、数据、日志目录（如果不存在）
        String[] dirPaths = {Apps.getConfigPath(), Apps.getDataPath(), Apps.getLogPath()};
        for (String dirPath : dirPaths) {
            FileUtils.createDirIfAbsent(dirPath);
        }
        // 向系统属性设置应用名
        System.setProperty(APPLICATION_NAME_PROPERTY_NAME, Apps.getAppCode());
    }
}
