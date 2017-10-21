/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-21 23:56 创建
 */
package org.antframework.boot.log.initializer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.antframework.boot.log.LogContext;

import java.nio.charset.Charset;

/**
 * 日志工具
 */
public class LogUtils {

    /**
     * 构建格式化器
     *
     * @param logContext 日志上下文
     * @param pattern    日志格式
     * @return 格式化器
     */
    public static Encoder buildEncoder(LogContext logContext, String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(pattern);
        encoder.setCharset(Charset.forName("utf-8"));
        logContext.getConfigurator().start(encoder);

        return encoder;
    }

    /**
     * 构建滚动文件appender
     *
     * @param logContext 日志上下文
     * @param name       appender名称
     * @param encoder    格式化器
     * @param filePath   文件路径
     * @param policy     滚动策略
     * @param filters    过滤器
     * @return 滚动文件appender
     */
    public static RollingFileAppender buildRollingFileAppender(LogContext logContext,
                                                               String name,
                                                               Encoder encoder,
                                                               String filePath,
                                                               RollingPolicy policy,
                                                               Filter... filters) {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setName(name);
        appender.setEncoder(encoder);
        appender.setFile(filePath);
        policy.setParent(appender);
        appender.setRollingPolicy(policy);
        for (Filter filter : filters) {
            appender.addFilter(filter);
        }
        logContext.getConfigurator().start(appender);

        return appender;
    }

    /**
     * 构建基于文件大小和时间的滚动策略
     *
     * @param logContext      日志上下文
     * @param rollingFilePath 滚动文件路径
     * @param maxFileSize     单个文件最大容量
     * @param maxHistory      最多保存的文件个数（null表示不限制）
     * @param totalSizeCap    日志最大保存容量（null表示不限制）
     * @return 策略
     */
    public static SizeAndTimeBasedRollingPolicy buildSizeAndTimeBasedRollingPolicy(LogContext logContext,
                                                                                   String rollingFilePath,
                                                                                   String maxFileSize,
                                                                                   Integer maxHistory,
                                                                                   String totalSizeCap) {
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        policy.setFileNamePattern(rollingFilePath);
        policy.setMaxFileSize(FileSize.valueOf(maxFileSize));
        if (maxHistory != null) {
            policy.setMaxHistory(maxHistory);
        }
        if (totalSizeCap != null) {
            policy.setTotalSizeCap(FileSize.valueOf(totalSizeCap));
        }
        logContext.getConfigurator().start(policy);

        return policy;
    }

    /**
     * 构建基于级别的过滤器
     *
     * @param logContext 日志上下文
     * @param level      级别
     * @return 过滤器
     */
    public static ThresholdFilter buildThresholdFilter(LogContext logContext, Level level) {
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel(level.levelStr);
        logContext.getConfigurator().start(filter);

        return filter;
    }
}
