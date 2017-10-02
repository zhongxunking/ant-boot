/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:21 创建
 */
package org.antframework.boot.config.core;

import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.support.ListenerRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * 监听配置是否被修改触发器
 */
public class ListenConfigModifiedTrigger implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private List<ConfigListener> configListeners;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (configListeners == null) {
            return;
        }
        // 注册配置监听器
        ListenerRegistrar listenerRegistrar = ConfigContextHolder.get().getListenerRegistrar();
        for (ConfigListener listener : configListeners) {
            listenerRegistrar.register(listener);
        }
        // 开始监听配置
        ConfigContextHolder.get().listenConfigModified();
    }
}
