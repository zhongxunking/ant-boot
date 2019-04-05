/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:07 创建
 */
package org.antframework.boot.logging.core;

import ch.qos.logback.classic.LoggerContext;
import lombok.Getter;
import org.springframework.core.env.Environment;

/**
 * 日志上下文
 */
@Getter
public class LogContext {
    // logback的logger上下文
    private final LoggerContext context;
    // spring环境
    private final Environment environment;
    // 易用的logback配置器
    private final LogbackConfigurator configurator;

    public LogContext(LoggerContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
        this.configurator = new LogbackConfigurator(context);
    }
}
