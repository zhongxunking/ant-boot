/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:21 创建
 */
package org.antframework.boot.env.listener.annotation;

import org.antframework.boot.env.listener.annotation.support.ConfigDataType;
import org.antframework.event.annotation.listener.Listener;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 配置监听器
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Listener(dataType = ConfigDataType.class, priority = Integer.MAX_VALUE)
public @interface ConfigListener {
    /**
     * 被监听的应用id（默认为当前应用）
     */
    String appId() default "";

    /**
     * 优先级
     */
    @AliasFor(annotation = Listener.class, attribute = "priority")
    int priority() default Integer.MAX_VALUE;
}
