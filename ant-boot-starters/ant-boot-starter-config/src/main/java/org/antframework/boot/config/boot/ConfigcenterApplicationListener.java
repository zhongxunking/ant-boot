/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 21:25 创建
 */
package org.antframework.boot.config.boot;

import org.antframework.boot.config.ConfigContextHolder;
import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.configcenter.client.ConfigContext;
import org.antframework.configcenter.client.spring.ConfigcenterPropertySource;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.io.File;

/**
 * 配置中心应用监听器（将配置中心加入到environment）
 */
public class ConfigcenterApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        // 构造配置上下文
        ConfigContext configContext = new ConfigContext(buildInitParams(environment));
        // 将配置中心设置到environment中
        environment.getPropertySources().addLast(buildPropertySource(configContext));
        // 初始化配置上下文持有器
        ConfigContextHolder.init(configContext);
    }

    // 构建初始化参数
    private ConfigContext.InitParams buildInitParams(ConfigurableEnvironment environment) {
        ConfigcenterProperties properties = Contexts.buildProperties(ConfigcenterProperties.class);

        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setAppCode(Apps.getAppCode());
        initParams.setQueriedAppCode(Apps.getAppCode());
        initParams.setProfileCode(Contexts.getProfile());
        initParams.setServerUrl(properties.getServerUrl());
        initParams.setCacheFilePath(Apps.getConfigPath() + File.separator + String.format("configcenter-%s.properties", Contexts.getProfile()));
        initParams.setZkUrls(properties.getZkUrls().toArray(new String[0]));

        return initParams;
    }

    // 构建配置中心在environment中的属性资源
    private PropertySource buildPropertySource(ConfigContext configContext) {
        return new ConfigcenterPropertySource(ConfigcenterPropertySource.PROPERTY_SOURCE_NAME, configContext);
    }
}
