/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-21 21:47 创建
 */
package org.antframework.boot.lang;

import org.antframework.common.util.other.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 应用操作类
 */
public final class Apps {
    /**
     * 配置目录的key
     */
    public static final String CONFIG_PATH_KEY = "app.config-path";
    /**
     * 数据目录的key
     */
    public static final String DATA_PATH_KEY = "app.data-path";
    /**
     * 日志目录的key
     */
    public static final String LOG_PATH_KEY = "app.log-path";

    // 应用
    private static App app;

    /**
     * 初始化
     *
     * @param appId 应用id
     */
    public static void initApp(String appId) {
        app = new App();
        app.appId = appId;
        app.configPath = PropertyUtils.getProperty(CONFIG_PATH_KEY, String.format("/var/apps/%s/config", appId));
        app.dataPath = PropertyUtils.getProperty(DATA_PATH_KEY, String.format("/var/apps/%s/data", appId));
        app.logPath = PropertyUtils.getProperty(LOG_PATH_KEY, String.format("/var/apps/%s/log", appId));
    }

    /**
     * 获取应用id
     */
    public static String getAppId() {
        return app.appId;
    }

    /**
     * 获取本应用的配置目录
     */
    public static String getConfigPath() {
        return app.configPath;
    }

    /**
     * 获取本应用的数据目录
     */
    public static String getDataPath() {
        return app.dataPath;
    }

    /**
     * 获取本应用的日志目录
     */
    public static String getLogPath() {
        return app.logPath;
    }

    /**
     * 如果profile未被设置，则设置profile
     */
    public static void setProfileIfAbsent(String profile) {
        if (StringUtils.isEmpty(PropertyUtils.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME))) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }

    // 应用
    private static class App {
        // 应用id
        private String appId;
        // 配置目录
        private String configPath;
        // 数据目录
        private String dataPath;
        // 日志目录
        private String logPath;
    }
}
