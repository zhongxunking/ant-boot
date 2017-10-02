/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-25 22:16 创建
 */
package org.antframework.boot.config;

import org.antframework.boot.config.listener.ConfigListenerType;
import org.antframework.boot.config.listener.ConfigModifiedEvent;
import org.antframework.configcenter.client.ConfigListener;
import org.antframework.configcenter.client.core.ModifiedProperty;
import org.bekit.event.EventPublisher;
import org.bekit.event.bus.EventBusHolder;
import org.bekit.event.publisher.DefaultEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DefaultConfigListener implements ConfigListener {
    @Autowired
    private EventBusHolder eventBusHolder;
    private EventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        eventPublisher = new DefaultEventPublisher(eventBusHolder.getEventBus(ConfigListenerType.class));
    }

    @Override
    public void configModified(List<ModifiedProperty> modifiedProperties) {
        publish("", modifiedProperties);
    }

    private void publish(String prefixKey, List<ModifiedProperty> modifiedProperties) {
        Map<String, List<ModifiedProperty>> map = new HashMap<>();
        for (ModifiedProperty modifiedProperty : modifiedProperties) {
            if (modifiedProperty.getKey() == null) {
                continue;
            }
            String prefix = getPrefix(modifiedProperty.getKey());
            ModifiedProperty sub = new ModifiedProperty(modifiedProperty.getType(), getSubPropertyKey(modifiedProperty.getKey()), modifiedProperty.getOldValue(), modifiedProperty.getNewValue());
            List<ModifiedProperty> subs = map.get(prefix);
            if (subs == null) {
                subs = new ArrayList<>();
                map.put(prefix, subs);
            }
            subs.add(sub);
        }
        for (String key : map.keySet()) {
            publish(prefixKey + '.' + key, map.get(key));
        }
        eventPublisher.publish(new ConfigModifiedEvent(prefixKey, modifiedProperties));
    }

    private String getPrefix(String propertyKey) {
        int index = propertyKey.indexOf('.');
        if (index < 0) {
            return propertyKey;
        } else {
            return propertyKey.substring(0, index);
        }
    }

    private String getSubPropertyKey(String propertyKey) {
        int index = propertyKey.indexOf('.');
        if (index < 0) {
            return null;
        } else {
            return propertyKey.substring(index + 1);
        }
    }
}
