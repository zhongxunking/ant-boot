/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-21 18:50 创建
 */
package org.antframework.boot.logging.initializer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingPolicy;
import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.logging.LogInitializer;
import org.antframework.boot.logging.core.LogContext;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;

import java.io.File;

/**
 * info日志文件初始化器
 */
@Order(1)
public class InfoFileLogInitializer implements LogInitializer {
    /**
     * appender名称
     */
    public static final String APPENDER_NAME = "info-file";

    @Override
    public void initialize(LogContext logContext) {
        InfoFileLogProperties properties = Contexts.buildProperties(InfoFileLogProperties.class);
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
                policy,
                LogUtils.buildThresholdFilter(logContext, Level.INFO));
        // 将在appender配置到root下
        logContext.getConfigurator().root(null, appender);
    }

    /**
     * info日志文件的属性
     */
    @ConfigurationProperties(InfoFileLogProperties.PREFIX)
    public static class InfoFileLogProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "ant.logging.info-file";
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
         * 选填：文件名（默认${appCode}-info.log）
         */
        @NotBlank
        private String fileName = Apps.getAppCode() + "-info.log";
        /**
         * 选填：滚动文件名（默认${appCode}-info.log.%d{yyyyMMdd}-%i）
         */
        @NotBlank
        private String rollingFileName = Apps.getAppCode() + "-info.log.%d{yyyyMMdd}-%i";
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

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getRollingFileName() {
            return rollingFileName;
        }

        public void setRollingFileName(String rollingFileName) {
            this.rollingFileName = rollingFileName;
        }

        public String getMaxFileSize() {
            return maxFileSize;
        }

        public void setMaxFileSize(String maxFileSize) {
            this.maxFileSize = maxFileSize;
        }

        public Integer getMaxHistory() {
            return maxHistory;
        }

        public void setMaxHistory(Integer maxHistory) {
            this.maxHistory = maxHistory;
        }

        public String getTotalSizeCap() {
            return totalSizeCap;
        }

        public void setTotalSizeCap(String totalSizeCap) {
            this.totalSizeCap = totalSizeCap;
        }
    }
}
