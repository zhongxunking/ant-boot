/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-21 21:47 创建
 */
package org.antframework.boot.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 应用操作类
 */
public final class Apps {
    // 配置文件夹路径前缀
    private static final String CONFIG_PATH_PREFIX = "/var/config/";
    // 数据文件夹路径前缀
    private static final String DATA_PATH_PREFIX = "/var/data/";
    // 日志文件夹路径前缀
    private static final String LOG_PATH_PREFIX = "/var/log/";

    // 应用
    private static App app;

    /**
     * 初始化
     *
     * @param appCode  应用编码
     * @param httpPort http端口
     */
    public static void initApp(String appCode, int httpPort) {
        if (app != null) {
            throw new IllegalStateException("App已经初始化，不能重复初始化");
        }
        app = new App();
        app.appCode = appCode;
        app.httpPort = httpPort;
        app.configPath = CONFIG_PATH_PREFIX + appCode;
        app.dataPath = DATA_PATH_PREFIX + appCode;
        app.logPath = LOG_PATH_PREFIX + appCode;
    }

    /**
     * 获取应用编码
     */
    public static String getAppCode() {
        return app.appCode;
    }

    /**
     * 获取http端口
     */
    public static int getHttpPort() {
        return app.httpPort;
    }

    /**
     * 获取配置文件夹路径
     */
    public static String getConfigPath() {
        return app.configPath;
    }

    /**
     * 获取数据文件夹路径
     */
    public static String getDataPath() {
        return app.dataPath;
    }

    /**
     * 获取日志文件路径
     */
    public static String getLogPath() {
        return app.logPath;
    }

    /**
     * 如果profile未被设置，则设置profile
     */
    public static void setProfileIfNotExists(String profile) {
        String envPropertyName = AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME.toUpperCase().replace('.', '_');
        if (StringUtils.isEmpty(System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME))
                && StringUtils.isEmpty(System.getenv(envPropertyName))) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }

    // 应用
    private static class App {
        // 应用名
        private String appCode;
        // http端口
        private int httpPort;
        // 配置文件夹路径
        private String configPath;
        // 数据文件夹路径
        private String dataPath;
        // 日志文件夹路径
        private String logPath;
    }
}
