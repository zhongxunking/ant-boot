/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-25 00:00 创建
 */
package org.antframework.boot.core.boot;

import lombok.AllArgsConstructor;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.core.env.listener.ConfigListener;
import org.antframework.boot.core.env.listener.support.DefaultConfigListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * ant自动配置类
 */
@Configuration
@Import(DefaultConfigListener.class)
@AllArgsConstructor
public class AntAutoConfiguration {
    // 配置监听器
    private List<ConfigListener> listeners;

    // 初始化
    @PostConstruct
    public void init() {
        // 注册所有配置监听器
        for (ConfigListener listener : listeners) {
            Contexts.getConfigListeners().addListener(listener);
        }
    }
}
