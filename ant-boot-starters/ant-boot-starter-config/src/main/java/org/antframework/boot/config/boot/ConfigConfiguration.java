/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 18:36 创建
 */
package org.antframework.boot.config.boot;

import org.antframework.boot.config.ConfigContextHolder;
import org.antframework.configcenter.client.spring.support.DefaultConfigListener;
import org.antframework.configcenter.client.spring.support.ListenConfigModifiedTrigger;
import org.bekit.event.boot.EventBusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置中心配置类
 */
@Configuration
@ConditionalOnProperty(name = ConfigcenterProperties.LISTEN_ENABLE_PROPERTY_NAME, matchIfMissing = true)
@AutoConfigureAfter(EventBusAutoConfiguration.class)
public class ConfigConfiguration {
    // 监听属性被修改触发器
    @Bean
    public ListenConfigModifiedTrigger listenConfigModifiedTrigger(DefaultConfigListener defaultConfigListener) {
        return new ListenConfigModifiedTrigger(ConfigContextHolder.get(), defaultConfigListener);
    }

    // 默认的配置监听器
    @Bean
    public DefaultConfigListener defaultConfigListener() {
        return new DefaultConfigListener();
    }
}
