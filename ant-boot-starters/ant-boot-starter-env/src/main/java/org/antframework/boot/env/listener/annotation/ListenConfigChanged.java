/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:59 创建
 */
package org.antframework.boot.env.listener.annotation;

import org.bekit.event.annotation.listener.Listen;
import org.bekit.event.listener.PriorityType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 监听配置被修改
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Listen(resolver = ListenConfigChangedResolver.class, priorityType = PriorityType.ASC)
public @interface ListenConfigChanged {
    /**
     * 被监听的配置key前缀（默认监听所有配置key）
     */
    String prefix() default "";

    /**
     * 优先级类型
     */
    @AliasFor(annotation = Listen.class, attribute = "priorityType")
    PriorityType priorityType() default PriorityType.ASC;
}
