/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:44 创建
 */
package org.antframework.boot.log.initializer;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.log.LogInitializer;
import org.antframework.boot.log.core.LogContext;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;

/**
 * 控制台日志初始化器
 */
@Order(1)
public class ConsoleLogInitializer implements LogInitializer {
    /**
     * appender名称
     */
    public static final String APPENDER_NAME = "console";

    @Override
    public void init(LogContext logContext) {
        ConsoleLogProperties properties = Contexts.buildProperties(ConsoleLogProperties.class);
        if (!properties.isEnable()) {
            return;
        }
        // 构建格式化器
        Encoder encoder = LogUtils.buildEncoder(logContext, properties.getPattern());
        // 构建appender
        Appender appender = buildAppender(logContext, encoder);
        // 将appender配置到root下
        logContext.getConfigurator().root(null, appender);
    }

    // 构建appender
    private Appender buildAppender(LogContext logContext, Encoder encoder) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setName(APPENDER_NAME);
        appender.setEncoder(encoder);
        logContext.getConfigurator().start(appender);

        return appender;
    }

    /**
     * 控制台日志属性
     */
    @ConfigurationProperties(ConsoleLogProperties.PREFIX)
    public static class ConsoleLogProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "ant.logging.console";
        /**
         * 默认的日志格式
         */
        public static final String DEFAULT_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%-5level) %clr([%thread]){faint} %clr(%logger{0}){cyan}%clr(:%L){faint}%clr(-){red} %msg%n%wEx";

        /**
         * 选填：是否开启（默认开启）
         */
        private boolean enable = true;
        /**
         * 选填：日志格式
         */
        @NotBlank
        private String pattern = DEFAULT_PATTERN;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }
}
