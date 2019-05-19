/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-25 00:00 创建
 */
package org.antframework.boot.env.boot;

import lombok.AllArgsConstructor;
import org.antframework.boot.env.Envs;
import org.antframework.boot.env.listener.ConfigListener;
import org.antframework.boot.env.listener.support.DefaultConfigListener;
import org.antframework.boot.env.refresh.placeholder.PlaceholdersRefresher;
import org.antframework.boot.env.refresh.properties.PropertiesTargetSourceCreator;
import org.antframework.boot.env.refresh.properties.PropertiesTargetSourceCreatorInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 环境自动配置类
 */
@Configuration
@Import(DefaultConfigListener.class)
@AllArgsConstructor
public class EnvAutoConfiguration {
    // 配置监听器
    private List<ConfigListener> listeners;

    // 初始化
    @PostConstruct
    public void init() {
        // 注册所有配置监听器
        for (ConfigListener listener : listeners) {
            Envs.getConfigListeners().addListener(listener);
        }
    }

    /**
     * 占位符刷新器配置类
     */
    @Configuration
    @ConditionalOnProperty(name = "ant.env.refresh-placeholders.enable", havingValue = "true", matchIfMissing = true)
    @Import(PlaceholdersRefresher.class)
    public static class PlaceholdersRefresherConfiguration {
    }

    /**
     * Properties刷新配置类
     */
    @Configuration
    @ConditionalOnProperty(name = "ant.env.refresh-properties.enable", havingValue = "true", matchIfMissing = true)
    @Import({PropertiesTargetSourceCreator.class, PropertiesTargetSourceCreatorInitializer.class})
    public static class PropertiesTargetSourceCreatorConfiguration {
    }
}
