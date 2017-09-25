/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 21:25 创建
 */
package org.antframework.boot.config;

import org.antframework.boot.core.Apps;
import org.antframework.configcenter.client.ConfigContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

/**
 *
 */
public class ConfigcenterApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    public static final String SERVER_URL_PROPERTY_NAME = "configcenter.serverUrl";
    public static final String ZK_URL_PROPERTY_NAME = "configcenter.zkUrl";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();

        ConfigContext configContext = new ConfigContext(buildInitParams(environment));
        configContext.getListenerRegistrar().register(new DefaultConfigListener());
        environment.getPropertySources().addLast(buildPropertySource(configContext));
        ConfigContextHolder.set(configContext);
    }

    private ConfigContext.InitParams buildInitParams(ConfigurableEnvironment environment) {
        String serverUrl = environment.getProperty(SERVER_URL_PROPERTY_NAME);
        if (StringUtils.isBlank(serverUrl)) {
            throw new IllegalArgumentException("未设置配置中心服务端地址：" + SERVER_URL_PROPERTY_NAME);
        }
        String zkUrl = environment.getProperty(ZK_URL_PROPERTY_NAME);
        if (StringUtils.isBlank(zkUrl)) {
            throw new IllegalArgumentException("未设置配置中心zookeeper地址：" + ZK_URL_PROPERTY_NAME);
        }

        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setAppCode(Apps.getAppCode());
        initParams.setQueriedAppCode(Apps.getAppCode());
        initParams.setProfileCode(environment.getActiveProfiles()[0]);
        initParams.setServerUrl(serverUrl);
        initParams.setCacheFilePath(Apps.getConfigPath() + "/configcenter.properties");
        initParams.setZkUrl(zkUrl);

        return initParams;
    }

    private PropertySource buildPropertySource(ConfigContext configContext) {
        return new ConfigcenterPropertySource(ConfigcenterPropertySource.PROPERTY_SOURCE_NAME, configContext.getProperties());
    }
}
