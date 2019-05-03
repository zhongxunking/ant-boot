/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.core.boot;

import org.antframework.boot.core.Contexts;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * ant应用RunListener
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AntApplicationRunListener implements SpringApplicationRunListener {
    // spring应用
    private SpringApplication springApplication;
    // 应用启动参数
    private ApplicationArguments arguments;

    public AntApplicationRunListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        this.arguments = new DefaultApplicationArguments(args);
    }

    @Override
    public void starting() {
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
    public void started(ConfigurableApplicationContext context) {
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
    }

    // 兼容SpringBoot1.x
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
    }
}
