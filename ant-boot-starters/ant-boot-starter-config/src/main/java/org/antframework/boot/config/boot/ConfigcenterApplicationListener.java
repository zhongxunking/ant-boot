/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-03 10:10 创建
 */
package org.antframework.boot.config.boot;

import org.antframework.boot.core.Apps;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 配置中心应用监听器
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConfigcenterApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    // 配置中心使用的应用id属性名
    private static final String APP_ID_PROPERTY_NAME = "configcenter.app-id";
    // 配置中心缓存目录属性名
    private static final String CACHE_DIR_PROPERTY_NAME = "configcenter.cache-dir";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.setProperty(APP_ID_PROPERTY_NAME, Apps.getAppId());
        System.setProperty(CACHE_DIR_PROPERTY_NAME, Apps.getConfigPath());
    }
}
