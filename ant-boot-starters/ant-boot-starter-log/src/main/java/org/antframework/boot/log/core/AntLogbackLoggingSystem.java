/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-19 19:38 创建
 */
package org.antframework.boot.log.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import org.antframework.boot.log.LogInitializer;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.logback.*;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;

import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;

/**
 * ant-boot日志系统
 */
public class AntLogbackLoggingSystem extends LogbackLoggingSystem {

    public AntLogbackLoggingSystem(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
        LoggerContext context = getLoggerContext();
        stopAndReset(context);
        // 构建日志上下文
        LogContext logContext = new LogContext(context, initializationContext.getEnvironment());
        // 基本配置
        baseConfig(logContext.getConfigurator());
        // 初始化日志
        initLog(logContext);
        context.setPackagingDataEnabled(true);
    }

    // 初始化日志
    private void initLog(LogContext logContext) {
        // 加载日志初始化器
        List<LogInitializer> initializers = SpringFactoriesLoader.loadFactories(LogInitializer.class, getClassLoader());
        for (LogInitializer initializer : initializers) {
            // 初始化
            initializer.init(logContext);
        }
    }

    // 基本配置（参考：DefaultLogbackConfiguration#base）
    private void baseConfig(LogbackConfigurator config) {
        config.conversionRule("clr", ColorConverter.class);
        config.conversionRule("wex", WhitespaceThrowableProxyConverter.class);
        config.conversionRule("wEx", ExtendedWhitespaceThrowableProxyConverter.class);
        LevelRemappingAppender debugRemapAppender = new LevelRemappingAppender(
                "org.springframework.boot");
        config.start(debugRemapAppender);
        config.appender("DEBUG_LEVEL_REMAPPER", debugRemapAppender);
        config.logger("org.apache.catalina.startup.DigesterFactory", Level.ERROR);
        config.logger("org.apache.catalina.util.LifecycleBase", Level.ERROR);
        config.logger("org.apache.coyote.http11.Http11NioProtocol", Level.WARN);
        config.logger("org.apache.sshd.common.util.SecurityUtils", Level.WARN);
        config.logger("org.apache.tomcat.util.net.NioSelectorPool", Level.WARN);
        config.logger("org.crsh.plugin", Level.WARN);
        config.logger("org.crsh.ssh", Level.WARN);
        config.logger("org.eclipse.jetty.util.component.AbstractLifeCycle", Level.ERROR);
        config.logger("org.hibernate.validator.internal.util.Version", Level.WARN);
        config.logger("org.springframework.boot.actuate.autoconfigure."
                + "CrshAutoConfiguration", Level.WARN);
        config.logger("org.springframework.boot.actuate.endpoint.jmx", null, false,
                debugRemapAppender);
        config.logger("org.thymeleaf", null, false, debugRemapAppender);
        // 设置root为info级别（可以通过属性logging.level.root覆盖）
        config.root(Level.INFO);
    }

    //------ 以下方法由于在LogbackLoggingSystem中是私有的，不能调用，所以拷贝过来 ------
    private LoggerContext getLoggerContext() {
        ILoggerFactory factory = StaticLoggerBinder.getSingleton().getLoggerFactory();
        Assert.isInstanceOf(LoggerContext.class, factory,
                String.format(
                        "LoggerFactory is not a Logback LoggerContext but Logback is on "
                                + "the classpath. Either remove Logback or the competing "
                                + "implementation (%s loaded from %s). If you are using "
                                + "WebLogic you will need to add 'org.slf4j' to "
                                + "prefer-application-packages in WEB-INF/weblogic.xml",
                        factory.getClass(), getLocation(factory)));
        return (LoggerContext) factory;
    }

    private Object getLocation(ILoggerFactory factory) {
        try {
            ProtectionDomain protectionDomain = factory.getClass().getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                return codeSource.getLocation();
            }
        } catch (SecurityException ex) {
            // Unable to determine location
        }
        return "unknown location";
    }

    private void stopAndReset(LoggerContext loggerContext) {
        loggerContext.stop();
        loggerContext.reset();
        if (isBridgeHandlerAvailable()) {
            addLevelChangePropagator(loggerContext);
        }
    }

    private void addLevelChangePropagator(LoggerContext loggerContext) {
        LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
        levelChangePropagator.setResetJUL(true);
        levelChangePropagator.setContext(loggerContext);
        loggerContext.addListener(levelChangePropagator);
    }
}
