/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-19 22:17 创建
 */
package org.antframework.boot.logging.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.PropertyContainer;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * spring-boot原生的org.springframework.boot.logging.logback.LogbackConfigurator很好用，但由于不是公开的不能直接用，所以复制一份进行使用
 */
public class LogbackConfigurator {

    private LoggerContext context;

    LogbackConfigurator(LoggerContext context) {
        Assert.notNull(context, "Context must not be null");
        this.context = context;
    }

    public PropertyContainer getContext() {
        return this.context;
    }

    public Object getConfigurationLock() {
        return this.context.getConfigurationLock();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void conversionRule(String conversionWord,
                               Class<? extends Converter> converterClass) {
        Assert.hasLength(conversionWord, "Conversion word must not be empty");
        Assert.notNull(converterClass, "Converter class must not be null");
        Map<String, String> registry = (Map<String, String>) this.context
                .getObject(CoreConstants.PATTERN_RULE_REGISTRY);
        if (registry == null) {
            registry = new HashMap<>();
            this.context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, registry);
        }
        registry.put(conversionWord, converterClass.getName());
    }

    public void appender(String name, Appender<?> appender) {
        appender.setName(name);
        start(appender);
    }

    public void logger(String name, Level level) {
        logger(name, level, true);
    }

    public void logger(String name, Level level, boolean additive) {
        logger(name, level, additive, null);
    }

    public void logger(String name, Level level, boolean additive,
                       Appender<ILoggingEvent> appender) {
        Logger logger = this.context.getLogger(name);
        if (level != null) {
            logger.setLevel(level);
        }
        logger.setAdditive(additive);
        if (appender != null) {
            logger.addAppender(appender);
        }
    }

    @SafeVarargs
    public final void root(Level level, Appender<ILoggingEvent>... appenders) {
        Logger logger = this.context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        if (level != null) {
            logger.setLevel(level);
        }
        for (Appender<ILoggingEvent> appender : appenders) {
            logger.addAppender(appender);
        }
    }

    public void start(LifeCycle lifeCycle) {
        if (lifeCycle instanceof ContextAware) {
            ((ContextAware) lifeCycle).setContext(this.context);
        }
        lifeCycle.start();
    }

}
