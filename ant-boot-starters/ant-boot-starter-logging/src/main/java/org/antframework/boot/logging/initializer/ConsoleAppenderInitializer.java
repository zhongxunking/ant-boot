/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:44 创建
 */
package org.antframework.boot.logging.initializer;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.logging.LoggingInitializer;
import org.antframework.boot.logging.core.LoggingContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * 控制台日志初始化器
 */
@Order(10)
public class ConsoleAppenderInitializer implements LoggingInitializer {
    /**
     * appender名称
     */
    public static final String APPENDER_NAME = "console";

    @Override
    public void init(LoggingContext context) {
        ConsoleAppenderProperties properties = Contexts.buildProperties(ConsoleAppenderProperties.class);
        if (!properties.isEnable()) {
            return;
        }
        // 构建格式化器
        Encoder encoder = LogUtils.buildEncoder(context, properties.getPattern());
        // 构建appender
        Appender appender = buildAppender(context, encoder);
        // 将appender配置到root下
        context.getConfigurator().root(null, appender);
    }

    // 构建appender
    private Appender buildAppender(LoggingContext context, Encoder encoder) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setName(APPENDER_NAME);
        appender.setEncoder(encoder);
        context.getConfigurator().start(appender);

        return appender;
    }

    /**
     * 控制台日志的配置
     */
    @ConfigurationProperties("ant.logging.console")
    @Validated
    @Getter
    @Setter
    public static class ConsoleAppenderProperties {
        /**
         * 默认的日志格式
         */
        public static final String DEFAULT_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%level) %clr([%thread]){faint} %clr(%logger{0}){cyan}%clr(:%L){faint}%clr(-){red} %msg%n%wEx";

        /**
         * 选填：是否开启（默认开启）
         */
        private boolean enable = true;
        /**
         * 选填：日志格式（默认%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%level) %clr([%thread]){faint} %clr(%logger{0}){cyan}%clr(:%L){faint}%clr(-){red} %msg%n%wEx）
         */
        @NotBlank
        private String pattern = DEFAULT_PATTERN;
    }
}
