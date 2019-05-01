/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.lang.boot;

import org.antframework.boot.core.Contexts;
import org.antframework.boot.lang.AntBootApplication;
import org.antframework.common.util.file.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

/**
 * Lang RunListener
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LangApplicationRunListener implements SpringApplicationRunListener {

    public LangApplicationRunListener(SpringApplication springApplication, String[] args) {
        initApp(springApplication);
    }

    @Override
    public void starting() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
    }

    // 初始化App
    private void initApp(SpringApplication springApplication) {
        // 查找@AntBootApplication
        AntBootApplication annotation = null;
        for (Object source : springApplication.getAllSources()) {
            if (!(source instanceof Class)) {
                continue;
            }
            annotation = AnnotatedElementUtils.findMergedAnnotation((Class) source, AntBootApplication.class);
            if (annotation != null) {
                break;
            }
        }
        Assert.notNull(annotation, "sources中无@AntBootApplication注解");
        // 设置应用id
        System.setProperty(Contexts.APP_ID_KEY, annotation.appId());
        // 创建应用home目录（如果不存在）
        FileUtils.createDirIfAbsent(Contexts.getHome());
    }
}
