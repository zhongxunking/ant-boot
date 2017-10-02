/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 21:25 创建
 */
package org.antframework.boot.config.core;

import org.antframework.boot.core.Apps;
import org.antframework.configcenter.client.ConfigContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

/**
 * 设置配置中心的应用监听器
 */
public class ConfigcenterApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    /**
     * 配置中心服务端地址
     */
    public static final String SERVER_URL_PROPERTY_NAME = "configcenter.serverUrl";
    /**
     * 配置中心使用的zookeeper地址（多个zookeeper以“,”分隔）
     */
    public static final String ZK_URL_PROPERTY_NAME = "configcenter.zkUrl";
    // 缓存文件名
    private static final String CACHE_FILE_NAME = "configcenter.properties";

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
        String serverUrl = environment.getProperty(SERVER_URL_PROPERTY_NAME);
        if (StringUtils.isBlank(serverUrl)) {
            throw new IllegalArgumentException("未设置配置中心服务端地址：" + SERVER_URL_PROPERTY_NAME);
        }
        String zkUrl = environment.getProperty(ZK_URL_PROPERTY_NAME);
        if (StringUtils.isBlank(zkUrl)) {
            throw new IllegalArgumentException("未设置配置中心使用的zookeeper地址：" + ZK_URL_PROPERTY_NAME);
        }

        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setAppCode(Apps.getAppCode());
        initParams.setQueriedAppCode(Apps.getAppCode());
        initParams.setProfileCode(environment.getActiveProfiles()[0]);
        initParams.setServerUrl(serverUrl);
        initParams.setCacheFilePath(Apps.getConfigPath() + "/" + CACHE_FILE_NAME);
        initParams.setZkUrl(zkUrl);

        return initParams;
    }

    // 构建配置中心在environment中的属性资源
    private PropertySource buildPropertySource(ConfigContext configContext) {
        return new ConfigcenterPropertySource(ConfigcenterPropertySource.PROPERTY_SOURCE_NAME, configContext.getProperties());
    }
}
