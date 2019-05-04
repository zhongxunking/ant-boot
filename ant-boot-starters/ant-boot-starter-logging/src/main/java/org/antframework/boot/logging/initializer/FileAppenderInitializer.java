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
import ch.qos.logback.core.util.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.logging.LoggingInitializer;
import org.antframework.boot.logging.core.LoggingContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * 日志文件初始化器
 */
@Order(10)
public class FileAppenderInitializer implements LoggingInitializer {
    /**
     * appender名称
     */
    public static final String APPENDER_NAME = "file";

    @Override
    public void init(LoggingContext context) {
        FileAppenderProperties properties = Contexts.buildProperties(FileAppenderProperties.class);
        if (!properties.isEnable()) {
            return;
        }
        // 构建格式化器
        Encoder encoder = LogUtils.buildEncoder(context, properties.getPattern());
        // 构建滚动策略
        RollingPolicy policy = LogUtils.buildSizeAndTimeBasedRollingPolicy(
                properties.getRollingFilePath(),
                new FileSize(properties.getMaxFileSize().toBytes()),
                properties.getMaxHistory(),
                properties.getTotalSizeCap());
        // 构建appender
        Appender appender = LogUtils.buildRollingFileAppender(context,
                APPENDER_NAME,
                encoder,
                properties.getFilePath(),
                policy);
        // 将appender配置到root下
        context.getConfigurator().root(null, appender);
    }

    /**
     * 日志文件的配置
     */
    @ConfigurationProperties("ant.logging.file")
    @Validated
    @Getter
    @Setter
    public static class FileAppenderProperties {
        /**
         * 默认的日志格式
         */
        public static final String DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread] %logger{0}:%L- %msg%n%wEx";

        /**
         * 选填：是否开启（默认开启）
         */
        private boolean enable = true;
        /**
         * 选填：日志格式（默认%d{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread] %logger{0}:%L- %msg%n%wEx）
         */
        @NotBlank
        private String pattern = DEFAULT_PATTERN;
        /**
         * 选填：文件路径（默认${app.home}/logs/${appId}.log）
         */
        @NotBlank
        private String filePath = Contexts.getHome() + File.separator + "logs" + File.separator + Contexts.getAppId() + ".log";
        /**
         * 选填：滚动文件路径（默认${app.home}/logs/${appId}.log.%d{yyyyMMdd}-%i）
         */
        @NotBlank
        private String rollingFilePath = Contexts.getHome() + File.separator + "logs" + File.separator + Contexts.getAppId() + ".log.%d{yyyyMMdd}-%i";
        /**
         * 选填：单个文件最大容量（默认1GB）
         */
        @NotNull
        private DataSize maxFileSize = DataSize.ofGigabytes(1);
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
