/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:06 创建
 */
package org.antframework.boot.logging;

import org.antframework.boot.logging.core.LoggingContext;

/**
 * 日志初始化器（实现类必须具有默认构造函数）
 */
@FunctionalInterface
public interface LoggingInitializer {
    /**
     * 初始化
     *
     * @param context 日志上下文
     */
    void init(LoggingContext context);
}
