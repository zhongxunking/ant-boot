/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-22 22:55 创建
 */
package org.antframework.boot.logging.boot;

import org.antframework.boot.logging.core.AntLogbackLoggingSystem;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * 设置ant-boot日志系统作为应用的日志系统的应用监听器（比{@link LoggingApplicationListener}先运行）
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER - 1)
public class AntLoggingApplicationListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        String loggingSystem = System.getProperty(LoggingSystem.SYSTEM_PROPERTY);
        if (loggingSystem == null) {
            // 如果未设置过日志系统，则设置ant-boot日志系统
            System.setProperty(LoggingSystem.SYSTEM_PROPERTY, AntLogbackLoggingSystem.class.getName());
        }
    }
}
