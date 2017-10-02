/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:12 创建
 */
package org.antframework.boot.config.core;

import org.antframework.configcenter.client.ConfigContext;

/**
 * 配置上下文持有器
 */
public class ConfigContextHolder {
    // 配置上下文
    private static ConfigContext configContext;

    /**
     * 初始化
     *
     * @param configContext 配置上下文
     */
    public static void init(ConfigContext configContext) {
        ConfigContextHolder.configContext = configContext;
    }

    /**
     * 获取配置上下文
     */
    public static ConfigContext get() {
        return configContext;
    }
}
