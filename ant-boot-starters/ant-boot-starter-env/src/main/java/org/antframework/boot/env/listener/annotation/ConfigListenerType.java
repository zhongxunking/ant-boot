/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:23 创建
 */
package org.antframework.boot.env.listener.annotation;

import org.bekit.event.extension.EventTypeResolver;
import org.bekit.event.extension.ListenerType;

/**
 * 配置监听器类型
 */
public class ConfigListenerType implements ListenerType {
    @Override
    public EventTypeResolver getResolver() {
        return ConfigEventTypeResolver.INSTANCE;
    }

    // 配置事件类型解决器
    private static class ConfigEventTypeResolver implements EventTypeResolver {
        // 实例
        private static final ConfigEventTypeResolver INSTANCE = new ConfigEventTypeResolver();

        @Override
        public Object resolve(Object event) {
            if (!(event instanceof ConfigChangedEvent)) {
                throw new IllegalArgumentException("无法识别的事件：" + event);
            }
            ConfigChangedEvent configChangedEvent = (ConfigChangedEvent) event;
            return new ConfigChangedEventType(configChangedEvent.getAppId(), configChangedEvent.getPrefix());
        }
    }
}
