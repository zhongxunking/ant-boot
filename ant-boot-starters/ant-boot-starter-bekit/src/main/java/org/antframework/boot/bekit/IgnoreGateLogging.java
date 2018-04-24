/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-04-23 20:59 创建
 */
package org.antframework.boot.bekit;

import java.lang.annotation.*;

/**
 * 忽略打印出入口日志
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreGateLogging {
}
