/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-27 18:32 创建
 */
package org.antframework.boot.logging.boot;

import lombok.Getter;
import lombok.Setter;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.annotation.ConfigListener;
import org.antframework.boot.env.listener.annotation.ListenConfigChanged;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理日志级别的监听器
 */
@ConfigListener
public class LoggingLevelApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    // 当前日志级别
    private Map<String, LogLevel> levels = new HashMap<>();

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        refreshLevels();
    }

    @ListenConfigChanged(prefix = "logging")
    public void onChange(List<ChangedProperty> changedProperties) {
        refreshLevels();
    }

    // 刷新日志级别
    private synchronized void refreshLevels() {
        LevelProperties properties = Contexts.buildProperties(LevelProperties.class);
        // 构建最新的日志级别
        Map<String, LogLevel> nextLevels = new HashMap<>();
        nextLevels.put(LoggingSystem.ROOT_LOGGER_NAME, LogLevel.INFO);
        properties.getLevel().forEach((name, level) -> {
            if (properties.getGroup().containsKey(name)) {
                for (String groupedName : properties.getGroup().get(name)) {
                    nextLevels.put(groupedName, level);
                }
            } else {
                nextLevels.put(name, level);
            }
        });
        // 重设日志级别
        LoggingSystem system = LoggingSystem.get(getClass().getClassLoader());
        nextLevels.forEach(system::setLogLevel);
        // 删除多余的日志级别
        levels.forEach((name, level) -> {
            if (!nextLevels.containsKey(name)) {
                system.setLogLevel(name, null);
            }
        });

        levels = nextLevels;
    }

    // 日志级别配置
    @ConfigurationProperties("logging")
    @Getter
    @Setter
    private static class LevelProperties {
        // 级别
        private Map<String, LogLevel> level = new HashMap<>();
        // 分组
        private Map<String, String[]> group = new HashMap<>();
    }
}
