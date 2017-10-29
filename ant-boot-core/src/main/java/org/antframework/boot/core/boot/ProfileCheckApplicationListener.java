/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-29 15:45 创建
 */
package org.antframework.boot.core.boot;

import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * 校验环境的应用监听器
 */
@Order(ConfigFileApplicationListener.DEFAULT_ORDER + 1)
public class ProfileCheckApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        if (event.getEnvironment().getActiveProfiles().length != 1) {
            throw new IllegalStateException("profile必须设置，且必须为一个");
        }
    }
}
