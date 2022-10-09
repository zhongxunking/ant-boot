/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-23 17:42 创建
 */
package org.antframework.boot.logging.initializer;

import ch.qos.logback.classic.Level;
import org.antframework.boot.logging.LoggingInitializer;
import org.antframework.boot.logging.core.LogbackConfigurator;
import org.antframework.boot.logging.core.LoggingContext;
import org.springframework.core.annotation.Order;

/**
 * 日志级别初始化器
 */
@Order(0)
public class LevelInitializer implements LoggingInitializer {
    @Override
    public void init(LoggingContext context) {
        LogbackConfigurator config = context.getConfigurator();
        // 设置root为info级别（可以通过属性logging.level.root覆盖）
        config.root(Level.INFO);
        // 设置特定logger级别
        config.logger("org.apache.zookeeper", Level.WARN);
        config.logger("org.apache.curator", Level.WARN);
        // 参考：DefaultLogbackConfiguration#defaults
        config.logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        config.logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        config.logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
        config.logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        config.logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        config.logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        config.logger("org.hibernate.validator.internal.util.Version", Level.WARN);
        config.logger("org.springframework.boot.actuate.endpoint.jmx", Level.WARN);
    }
}
