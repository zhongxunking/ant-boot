/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-21 18:50 创建
 */
package org.antframework.boot.logging.initializer;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingPolicy;
import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.logging.LogInitializer;
import org.antframework.boot.logging.core.LogContext;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;

import java.io.File;

/**
 * 日志文件初始化器
 */
@Order(1)
public class FileLogInitializer implements LogInitializer {
    /**
     * appender名称
     */
    public static final String APPENDER_NAME = "file";

    @Override
    public void initialize(LogContext logContext) {
        FileLogProperties properties = Contexts.buildProperties(FileLogProperties.class);
        if (!properties.isEnable()) {
            return;
        }
        // 构建格式化器
        Encoder encoder = LogUtils.buildEncoder(logContext, properties.getPattern());
        // 构建滚动策略
        RollingPolicy policy = LogUtils.buildSizeAndTimeBasedRollingPolicy(
                Apps.getLogPath() + File.separator + properties.getRollingFileName(),
                properties.getMaxFileSize(),
                properties.getMaxHistory(),
                properties.getTotalSizeCap());
        // 构建appender
        Appender appender = LogUtils.buildRollingFileAppender(logContext,
                APPENDER_NAME,
                encoder,
                Apps.getLogPath() + File.separator + properties.getFileName(),
                policy);
        // 将在appender配置到root下
        logContext.getConfigurator().root(null, appender);
    }

    /**
     * 日志文件的属性
     */
    @ConfigurationProperties(FileLogProperties.PREFIX)
    @Getter
    @Setter
    public static class FileLogProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "ant.logging.file";
        /**
         * 默认的日志格式
         */
        public static final String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread] %logger{0}:%L- %msg%n%wEx";

        /**
         * 选填：是否开启（默认开启）
         */
        private boolean enable = true;
        /**
         * 选填：日志格式
         */
        @NotBlank
        private String pattern = DEFAULT_PATTERN;
        /**
         * 选填：文件名（默认${appId}.log）
         */
        @NotBlank
        private String fileName = Apps.getAppId() + ".log";
        /**
         * 选填：滚动文件名（默认${appId}.log.%d{yyyyMMdd}-%i）
         */
        @NotBlank
        private String rollingFileName = Apps.getAppId() + ".log.%d{yyyyMMdd}-%i";
        /**
         * 选填：单个文件最大容量
         */
        @NotBlank
        private String maxFileSize = "1GB";
        /**
         * 选填：最多保存的文件个数（默认不限制）
         */
        private Integer maxHistory;
        /**
         * 选填：日志最大保存容量（默认不限制）
         */
        private String totalSizeCap;
    }
}
