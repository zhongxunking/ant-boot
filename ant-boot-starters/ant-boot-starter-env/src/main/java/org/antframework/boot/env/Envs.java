/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-27 12:09 创建
 */
package org.antframework.boot.env;

import org.antframework.boot.env.listener.support.ConfigListenerHub;

/**
 * 环境操作类
 */
public final class Envs {
    // 配置监听器注册器
    private static final ConfigListenerHub CONFIG_LISTENER_HUB = new ConfigListenerHub();

    /**
     * 获取配置监听器中心
     */
    public static ConfigListenerHub getConfigListenerHub() {
        return CONFIG_LISTENER_HUB;
    }
}
