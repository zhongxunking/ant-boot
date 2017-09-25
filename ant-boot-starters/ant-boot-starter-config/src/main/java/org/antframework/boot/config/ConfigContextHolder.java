/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:12 创建
 */
package org.antframework.boot.config;

import org.antframework.configcenter.client.ConfigContext;

/**
 *
 */
public class ConfigContextHolder {

    private static ConfigContext configContext;

    public static void set(ConfigContext configContext) {
        ConfigContextHolder.configContext = configContext;
    }

    public static ConfigContext get() {
        return configContext;
    }
}
