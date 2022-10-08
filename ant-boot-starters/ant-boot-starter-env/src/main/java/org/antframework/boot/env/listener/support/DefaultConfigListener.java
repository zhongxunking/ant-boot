/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:16 创建
 */
package org.antframework.boot.env.listener.support;

import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.ConfigListener;
import org.antframework.boot.env.listener.annotation.ConfigChangedEvent;
import org.antframework.boot.env.listener.annotation.ConfigListenerType;
import org.antframework.event.EventPublisher;
import org.antframework.event.bus.EventBusHub;
import org.antframework.event.publisher.DefaultEventPublisher;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的配置监听器（将配置变更消息通知到@ConfigListener监听器）
 */
@Order(DefaultConfigListener.ORDER)
public class DefaultConfigListener implements ConfigListener {
    /**
     * 优先级
     */
    public static final int ORDER = 0;
    // key的分隔符
    private static final char KEY_SEPARATOR = '.';

    // 事件发布器
    private final EventPublisher eventPublisher;

    public DefaultConfigListener(EventBusHub eventBusHub) {
        eventPublisher = new DefaultEventPublisher(eventBusHub.getEventBus(ConfigListenerType.class));
    }

    @Override
    public void onChange(String appId, List<ChangedProperty> changedProperties) {
        dispatch(appId, null, changedProperties);
    }

    // 将被修改的配置按照key前缀进行递归分派
    private void dispatch(String appId, String prefixKey, List<ChangedProperty> cps) {
        Map<String, List<ChangedProperty>> dispatchedCps = new HashMap<>();
        // 根据配置key前缀进行分拣
        for (ChangedProperty cp : cps) {
            if (cp.getKey() == null) {
                continue;
            }
            String prefix = getPrefix(cp.getKey());
            ChangedProperty nextCp = new ChangedProperty(cp.getType(), getSuffix(cp.getKey()), cp.getOldValue(), cp.getNewValue());
            List<ChangedProperty> nextCps = dispatchedCps.computeIfAbsent(prefix, key -> new ArrayList<>());
            nextCps.add(nextCp);
        }
        // 将分拣过的配置通过递归继续分拣
        dispatchedCps.forEach((prefix, nextCps) -> {
            String nextPrefixKey = prefixKey == null ? prefix : prefixKey + KEY_SEPARATOR + prefix;
            dispatch(appId, nextPrefixKey, nextCps);
        });
        // 发送事件
        eventPublisher.publish(new ConfigChangedEvent(appId, prefixKey, cps));
    }

    // 获取前缀（aa.bb.cc返回aa）
    private String getPrefix(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return key;
        } else {
            return key.substring(0, index);
        }
    }

    // 获取后缀（aa.bb.cc返回bb.cc）
    private String getSuffix(String key) {
        int index = key.indexOf(KEY_SEPARATOR);
        if (index < 0) {
            return null;
        } else {
            return key.substring(index + 1);
        }
    }
}
