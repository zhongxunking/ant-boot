/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:59 创建
 */
package org.antframework.boot.config.annotation;

import org.antframework.boot.config.listener.ListenConfigModifiedResolver;
import org.bekit.event.annotation.listener.Listen;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Listen(resolver = ListenConfigModifiedResolver.class)
public @interface ListenConfigModified {

    String propertyNamePrefix();
}
