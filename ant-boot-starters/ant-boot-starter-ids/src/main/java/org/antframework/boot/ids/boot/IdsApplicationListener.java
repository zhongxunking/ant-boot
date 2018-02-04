/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-05 00:25 创建
 */
package org.antframework.boot.ids.boot;

import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.ids.IdsParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * ids应用监听器
 */
@Order(LoggingApplicationListener.DEFAULT_ORDER + 2)
public class IdsApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    // 应用端口属性名
    private static final String SERVER_PORT_PROPERTY_NAME = "server.port";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        IdsProperties properties = Contexts.buildProperties(IdsProperties.class);
        // 设置ids初始化所需要的数据
        System.setProperty(IdsParams.APP_CODE_PROPERTY_NAME, Apps.getAppCode());
        System.setProperty(IdsParams.APP_PORT_PROPERTY_NAME, event.getEnvironment().getProperty(SERVER_PORT_PROPERTY_NAME));
        System.setProperty(IdsParams.SERVER_URL_PROPERTY_NAME, properties.getServerUrl());
        System.setProperty(IdsParams.ZK_URLS_PROPERTY_NAME, StringUtils.join(properties.getZkUrls(), ','));
        System.setProperty(IdsParams.HOME_PATH_PROPERTY_NAME, Apps.getConfigPath());
    }
}
