/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 16:01 创建
 */
package org.antframework.boot.env.listener.annotation.support;

import org.antframework.boot.core.Contexts;
import org.antframework.boot.env.listener.ChangedProperty;
import org.antframework.boot.env.listener.annotation.ConfigListener;
import org.antframework.boot.env.listener.annotation.ListenConfigChanged;
import org.antframework.event.annotation.listener.ListenResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 监听配置被修改解决器
 */
public class ListenConfigChangedResolver implements ListenResolver {
    // 监听方法
    private Method listenMethod;
    // 事件类型
    private ConfigChangedEventType eventType;
    // 监听的是否是单个key
    private boolean singleKey;

    @Override
    public void init(Method listenMethod) {
        ConfigListener configListenerAnnotation = AnnotatedElementUtils.findMergedAnnotation(listenMethod.getDeclaringClass(), ConfigListener.class);
        if (configListenerAnnotation == null) {
            throw new IllegalArgumentException("@ListenConfigModified只能标注在配置监听器（@ConfigListener）的方法上");
        }
        this.listenMethod = listenMethod;
        // 计算被监听的应用id
        String appId = configListenerAnnotation.appId();
        if (StringUtils.isEmpty(appId)) {
            appId = Contexts.getAppId();
        }
        // 校验入参
        Class[] parameterTypes = listenMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）或（ModifiedProperty）");
        }
        if (parameterTypes[0] == ChangedProperty.class) {
            singleKey = true;
        } else {
            if (parameterTypes[0] != List.class) {
                throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）或（ModifiedProperty）");
            }
            ResolvableType resolvableType = ResolvableType.forMethodParameter(listenMethod, 0);
            if (resolvableType.getGeneric(0).resolve(Object.class) != ChangedProperty.class) {
                throw new RuntimeException("监听配置被修改方法" + ClassUtils.getQualifiedMethodName(listenMethod) + "的入参必须是（List<ModifiedProperty>）或（ModifiedProperty）");
            }
            singleKey = false;
        }
        // 设置事件类型
        ListenConfigChanged listenConfigChangedAnnotation = AnnotatedElementUtils.findMergedAnnotation(listenMethod, ListenConfigChanged.class);
        String prefix = listenConfigChangedAnnotation.prefix();
        if (StringUtils.isEmpty(prefix)) {
            prefix = null;
        }
        eventType = new ConfigChangedEventType(appId, prefix);
    }

    @Override
    public Object getEventType() {
        return eventType;
    }

    @Override
    public Object[] resolveParams(Object event) {
        List<ChangedProperty> changedProperties = ((ConfigChangedEvent) event).getChangedProperties();
        Object param;
        if (!singleKey) {
            param = changedProperties;
        } else {
            if (changedProperties.size() > 1) {
                throw new IllegalStateException(String.format("有多个以[%s]为前缀的配置项有变更，监听方法[%s]入参无法传多个变更项，可以考虑将入参换成（List<ModifiedProperty>）形式。",
                        eventType.getPrefix(),
                        listenMethod.toString()));
            }
            param = changedProperties.get(0);
        }
        return new Object[]{param};
    }
}
