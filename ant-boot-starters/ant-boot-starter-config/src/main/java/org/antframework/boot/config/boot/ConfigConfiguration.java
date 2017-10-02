/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 18:36 创建
 */
package org.antframework.boot.config.boot;

import org.antframework.boot.config.DefaultConfigListener;
import org.antframework.boot.config.ListenConfigModifiedTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class ConfigConfiguration {

    @Bean
    public ListenConfigModifiedTrigger listenConfigModifiedTrigger() {
        return new ListenConfigModifiedTrigger();
    }

    @Bean
    public DefaultConfigListener defaultConfigListener() {
        return new DefaultConfigListener();
    }
}
