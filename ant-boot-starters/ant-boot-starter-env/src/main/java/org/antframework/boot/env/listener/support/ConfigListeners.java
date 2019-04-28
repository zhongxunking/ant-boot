/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-24 23:36 创建
 */
package org.antframework.boot.env.listener.support;

import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.ConfigListener;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置监听器的管理器
 */
public class ConfigListeners {
    // 监听器
    private List<ConfigListener> listeners = new ArrayList<>();

    /**
     * 添加监听器
     *
     * @param listener 需添加的监听器
     */
    public synchronized void addListener(ConfigListener listener) {
        if (!listeners.contains(listener)) {
            List<ConfigListener> nextListeners = new ArrayList<>(listeners);
            nextListeners.add(listener);
            AnnotationAwareOrderComparator.sort(nextListeners);
            listeners = nextListeners;
        }
    }

    /**
     * 删除监听器
     *
     * @param listener 需删除的监听器
     */
    public synchronized void removeListener(ConfigListener listener) {
        if (listeners.contains(listener)) {
            List<ConfigListener> nextListeners = new ArrayList<>(listeners);
            nextListeners.remove(listener);
            listeners = nextListeners;
        }
    }

    /**
     * 配置变更后通知监听器
     *
     * @param appId             配置变更的应用id
     * @param changedProperties 变更的配置
     */
    public synchronized void onChange(String appId, List<ChangedProperty> changedProperties) {
        for (ConfigListener listener : listeners) {
            listener.onChange(appId, changedProperties);
        }
    }
}
