/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-05-02 22:30 创建
 */
package org.antframework.boot.config;

import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.common.util.other.Cache;
import org.antframework.configcenter.client.ConfigContext;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.util.Set;

/**
 * 配置上下文操作类
 */
public class ConfigContexts {
    // 配置上下文缓存
    private static final Cache<String, ConfigContext> CACHE = new Cache<>(new Cache.Supplier<String, ConfigContext>() {
        @Override
        public ConfigContext get(String key) {
            return buildConfigContext(key);
        }
    });

    /**
     * 获取配置上下文
     *
     * @param appId 被查询配置的应用id
     */
    public static ConfigContext get(String appId) {
        return CACHE.get(appId);
    }

    /**
     * 获取已缓存的应用id
     */
    public static Set<String> getAppIds() {
        return CACHE.getAllKeys();
    }

    // 构建配置上下文
    private static ConfigContext buildConfigContext(String queriedAppId) {
        ConfigcenterProperties properties = Contexts.buildProperties(ConfigcenterProperties.class);

        ConfigContext.InitParams initParams = new ConfigContext.InitParams();
        initParams.setAppId(Apps.getAppId());
        initParams.setQueriedAppId(queriedAppId);
        initParams.setProfileId(Contexts.getProfile());
        initParams.setServerUrl(properties.getServerUrl());
        initParams.setCacheFilePath(Apps.getConfigPath() + File.separator + String.format("configcenter-%s-%s.properties", queriedAppId, Contexts.getProfile()));
        initParams.setZkUrls(properties.getZkUrls().toArray(new String[0]));

        return new ConfigContext(initParams);
    }

    /**
     * 配置中心属性
     */
    @ConfigurationProperties(prefix = ConfigcenterProperties.PREFIX)
    public static class ConfigcenterProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "configcenter";
        /**
         * 是否开启监听的属性名
         */
        public static final String LISTEN_ENABLE_PROPERTY_NAME = PREFIX + ".listen.enable";

        /**
         * 必填：配置中心服务端地址（比如：http://localhost:6220）
         */
        @NotBlank
        private String serverUrl;
        /**
         * 必填：配置中心使用的zookeeper地址，存在多个zookeeper的话以“,”分隔（比如：192.168.0.1:2181,192.168.0.2:2181）
         */
        @NotEmpty
        private Set<String> zkUrls;

        public String getServerUrl() {
            return serverUrl;
        }

        public void setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
        }

        public Set<String> getZkUrls() {
            return zkUrls;
        }

        public void setZkUrls(Set<String> zkUrls) {
            this.zkUrls = zkUrls;
        }
    }
}
