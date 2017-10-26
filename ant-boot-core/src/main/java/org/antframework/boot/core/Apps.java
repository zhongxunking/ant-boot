/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-21 21:47 创建
 */
package org.antframework.boot.core;

import org.antframework.common.util.system.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 应用操作类
 */
public final class Apps {
    /**
     * 配置目录属性名
     */
    public static final String CONFIG_PATH_PROPERTY_NAME = "app.config-path";
    /**
     * 数据目录属性名
     */
    public static final String DATA_PATH_PROPERTY_NAME = "app.data-path";
    /**
     * 日志目录属性名
     */
    public static final String LOG_PATH_PROPERTY_NAME = "app.log-path";

    // 应用
    private static App app;

    /**
     * 初始化
     *
     * @param appCode 应用编码
     */
    public static void initApp(String appCode) {
        if (app != null) {
            throw new IllegalStateException("App已经初始化，不能重复初始化");
        }
        app = new App();
        app.appCode = appCode;
        app.configPath = PropertyUtils.getProperty(CONFIG_PATH_PROPERTY_NAME, "/var/apps/config/" + appCode);
        app.dataPath = PropertyUtils.getProperty(DATA_PATH_PROPERTY_NAME, "/var/apps/data/" + appCode);
        app.logPath = PropertyUtils.getProperty(LOG_PATH_PROPERTY_NAME, "/var/apps/log/" + appCode);
    }

    /**
     * 获取应用编码
     */
    public static String getAppCode() {
        return app.appCode;
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
    public static void setProfileIfNotExists(String profile) {
        if (StringUtils.isEmpty(PropertyUtils.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME))) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }

    // 应用
    private static class App {
        // 应用编码
        private String appCode;
        // 配置目录
        private String configPath;
        // 数据目录
        private String dataPath;
        // 日志目录
        private String logPath;
    }
}
